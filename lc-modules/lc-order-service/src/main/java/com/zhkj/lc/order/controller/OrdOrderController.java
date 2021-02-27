package com.zhkj.lc.order.controller;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Date;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.gson.Gson;
import com.zhkj.lc.common.api.YunPianSMSUtils;
import com.zhkj.lc.common.constant.CommonConstant;
import com.zhkj.lc.common.util.*;
import com.zhkj.lc.common.util.http.HttpClientUtil;
import com.zhkj.lc.common.vo.*;
import com.zhkj.lc.order.dto.*;
import com.zhkj.lc.order.feign.CommonGoodsTruckFeign;
import com.zhkj.lc.order.feign.SystemFeginServer;
import com.zhkj.lc.order.feign.TrunkFeign;
import com.zhkj.lc.order.mapper.OrdOrderLogisticsMapper;
import com.zhkj.lc.order.mapper.OrderSettlementMapper;
import com.zhkj.lc.order.model.entity.*;
import com.zhkj.lc.order.service.IOrdCommonGoodsService;
import com.zhkj.lc.order.service.IOrdContainerTypeService;
import com.zhkj.lc.order.service.IOrdOrderLogisticsService;
import com.zhkj.lc.order.utils.CommonUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
//import net.sf.json.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.plugins.Page;
import com.zhkj.lc.order.service.IOrdOrderService;
import com.zhkj.lc.common.web.BaseController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.zhkj.lc.common.util.DateUtils.getFirstDate;
import static com.zhkj.lc.common.util.DateUtils.getLastDate;

/**
 * <p>
 * 订单管理 前端控制器
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
@RestController
@RequestMapping("/ordOrder")
public class OrdOrderController extends BaseController {
    @Autowired private IOrdOrderService ordOrderService;
    @Autowired private IOrdCommonGoodsService commonGoodsService;
    @Autowired private TrunkFeign trunkFeign;
    @Autowired private IOrdOrderLogisticsService logisticsService;
    @Autowired private CommonGoodsTruckFeign commonGoodsTruckFeign;

    @Autowired
    private SystemFeginServer systemFeginServer;
    @Resource
    private ResourceLoader resourceLoader;
    @Autowired
    private OrderSettlementMapper settlementMapper;
    @Autowired
    private OrdOrderLogisticsMapper ordOrderLogisticsMapper;
    /**
    * 通过ID查询
    *
    * @param orderId orderId
    * @return OrdOrder
    */
    @GetMapping("selectByOrderId/{orderId}")
    public OrdOrder  get(@PathVariable String orderId) {
        return ordOrderService.selectOrderById(orderId, getTenantId());
    }

    /**
    * 分页查询信息
    *
    * @param params 分页对象
    * @return 分页对象
    */
    @RequestMapping("/page")
    public Page page(@RequestParam Map<String, Object> params, OrderSearch orderSearch) {
        orderSearch.setTenantId(getTenantId());
        return ordOrderService.selectOrderList(new Query<>(params),orderSearch);
    }

    /**
     * 添加
     * @param  ordOrder  实体
     * @return success/false
     */
    private  OrdOrder fz(@RequestBody OrdOrder ordOrder){
        ordOrder.setTenantId(getTenantId());
        ordOrder.setCreateBy(UserUtils.getUser());
        ordOrder.setUpdateBy(UserUtils.getUser());
        ordOrder.setCreateTime(new Date());
        ordOrder.setUpdateTime(new Date());
        return  ordOrder;
    }
    @PostMapping
    @Transactional
    public R<Boolean> add(@RequestBody OrdOrder ordOrder) throws IOException {
        fz(ordOrder);
        ordOrder.setStatus(CommonConstant.ORDER_CG);
        BigDecimal rate = getRate();
        if(rate==null){
            return new R<>(Boolean.FALSE, "请先进行税率设置！");
        }
        ordOrder.setPayRate(rate);
        if(ordOrderService.insertOrder(ordOrder)!=null){
            return new R<>(Boolean.TRUE);
        }
        return new R<>(Boolean.FALSE);
    }
    //订单补录
    @PostMapping("/supplement")
    @Transactional
    @ApiOperation(value="新增功能 -- 订单补录")
    public R<Boolean> add2(@RequestBody OrdOrder ordOrder) throws IOException {
        fz(ordOrder);
        BigDecimal rate = getRate();
        if(rate==null){
            return new R<>(Boolean.FALSE, "请先进行税率设置！");
        }
        ordOrder.setPayRate(rate);

            ordOrder.setStatus("12");//
            /*新增结算数据*/
            OrderSettlement settlement = settlementMapper.selectSettlementByOrderId(ordOrder.getOrderId());
            if(settlement == null){
                settlement = new OrderSettlement();
                settlement.setOrderId(ordOrder.getOrderId());
                settlementMapper.insert(settlement);
            }
             ordOrderService.insertOrder(ordOrder);
        /*更新运踪信息*/
        for(int i=1;i<Integer.parseInt(ordOrder.getStatus());i++) {
            OrdOrderLogistics logistics = new OrdOrderLogistics();
            logistics.setLogisticsMsg("");
            logistics.setOrderId(ordOrder.getOrderId());
            logistics.setLogisticsAddress("");//logisticsMsg
            logistics.setOrderStatus(i+"");
            logistics.setLogisticsTime(new Date());
            logistics.setTenantId(getTenantId());
            OrdOrderLogistics log = ordOrderLogisticsMapper.selectByOrderIdAndStatus(ordOrder.getOrderId(), "3");
            if (log == null) {
                ordOrderLogisticsMapper.insertOrderLogistics(logistics);
            } else {
                logistics.setId(log.getId());
                ordOrderLogisticsMapper.updateById(logistics);
            }
        }
        return new R<>(true);
    }
    /**
     * 添加并发送司机
     * @param  ordOrder  实体
     * @return success/false
     */
    @ApiOperation("sdfsdf")
    @PostMapping("addAndSend")
    @Transactional
    public R<Boolean> addAndSend(@RequestBody OrdOrder ordOrder) throws IOException {
        BigDecimal rate = getRate();
        if(rate==null){
            return new R<>(Boolean.FALSE, "请先进行税率设置！");
        }
        ordOrder.setPayRate(rate);
        String orderId = ordOrder.getOrderId();
        ordOrder.setTenantId(getTenantId());
        ordOrder.setSendTruckDate(new Date());//派车日期
        ordOrder.setStatus(CommonConstant.ORDER_DJD);
        /*给司机派单前验证*/
        DriverVO driverVO = new DriverVO();
        if(ordOrder.getDriverId()!=null){
            driverVO = trunkFeign.selectDriverStatus(ordOrder.getDriverId(), ordOrder.getTenantId());
            if(driverVO == null){
                return new R<>(Boolean.FALSE,"该司机不存在！");
            }else {
                if(!driverVO.getStatus().equals("2")){
                    return new R<>(Boolean.FALSE,driverVO.getDriverName()+"该司机处于请假状态或者在途状态，不能接单！");
                }
                if(driverVO.getDelFlag().equals("1")){
                    return new R<>(Boolean.FALSE,driverVO.getDriverName()+"该司机已被删除！");
                }
                if(!driverVO.getPlateNumber().equals(ordOrder.getPlateNumber())){
                    return new R<>(Boolean.FALSE,driverVO.getDriverName()+"该司机和车辆信息不匹配！");
                }
            }
        }

        if(orderId == null){
            ordOrder.setTenantId(getTenantId());
            ordOrder.setCreateBy(UserUtils.getUser());
            ordOrder.setUpdateBy(UserUtils.getUser());
            ordOrder.setCreateTime(new Date());
            ordOrder.setUpdateTime(new Date());
            orderId = ordOrderService.insertOrder(ordOrder);
        }else {
            ordOrder.setUpdateBy(UserUtils.getUser());
            ordOrder.setUpdateTime(new Date());
            ordOrderService.update(ordOrder);
            /*上报公路系统司机车辆信息*/
            reportDriver(ordOrder);
        }
        if (systemFeginServer.selectIsSend(getTenantId())) {
            SysSmsTempVO s = systemFeginServer.selectSmsSetByTplId(getTenantId(), CommonConstant.TPL_ID_NEW);
            if ((!s.getIsSend().equals("1")) && (!s.getIsSendDriver().equals("1"))) {
                YunPianSMSUtils.sendDriverNewOrder(driverVO.getPhone(),orderId);
            }
        }

        OrdOrderLogistics logistics = new OrdOrderLogistics();
        logistics.setLogisticsMsg("订单正在派车中");
        logistics.setOrderId(orderId);
        logistics.setTenantId(getTenantId());
        logistics.setOrderStatus(CommonConstant.ORDER_DJD);
        //更新司机信息
        List<DriverVO> status = trunkFeign.selectDriverStatusByPlateNumber(ordOrder.getPlateNumber());
        Integer[] driverIds =  new Integer[status.size()];
        for(int i =0 ; i<status.size();i++){
            driverIds[i] = status.get(i).getDriverId();
        }
        DriverVO driver = new DriverVO();
        driver.setStatus(CommonConstant.SJZT);
        driver.setDriverIds(driverIds);
        commonGoodsTruckFeign.updateDriverSta(driver);
        OrdOrderLogistics log = logisticsService.selectByOrderIdAndStatus(logistics.getOrderId(),CommonConstant.ORDER_DJD);
        if(log==null){
            logisticsService.insertOrderLogistics(logistics);
        }else {
            logistics.setId(log.getId());
            logisticsService.updateById(logistics);
        }
        return new R<>(Boolean.TRUE);
    }

    /**
     * 删除
     * @param ids ID
     * @return success/false
     */
    @DeleteMapping("/{ids}")
    @Transactional
    public R<Boolean> delete(@PathVariable String ids) {
        return new R<>(ordOrderService.deleteOrderByIds(ids, getTenantId(), UserUtils.getUser()));
    }

    /**
     * 编辑
     * @param  ordOrder  实体
     * @return success/false
     */
    @PutMapping
    @Transactional
    public R<Boolean> edit(@RequestBody OrdOrder ordOrder) throws IOException {
        if(ordOrder.getPayRate() == null){
            BigDecimal rate = getRate();
            if(rate == null){
                return new R<>(Boolean.FALSE,"请先进行税率设置！");
            }
            ordOrder.setPayRate(rate);
        }

        ordOrder.setUpdateBy(UserUtils.getUser());
        return new R<>(ordOrderService.update(ordOrder));
    }

    @PutMapping("/addAnno")
    @Transactional
    @ApiOperation(value="新增功能 -- 修改已发送司机的集装箱订单 同时在小程序公告模块进行通知")
    public R<Boolean> edit2(@RequestBody OrdOrder ordOrder) throws IOException {
        edit(ordOrder);
        AnnouncementVO announcementVO=new AnnouncementVO ();
        announcementVO.setTitle("订单信息修改");
        announcementVO.setContent("订单编号为"+ordOrder.getOrderId()+"的集装箱订单已发生修改，请查看详细信息");
        announcementVO.setDriverOwerId(ordOrder.getDriverId());
        announcementVO.setType("0");
        announcementVO.setTenantId(getTenantId());
        announcementVO.setCreateBy(UserUtils.getUser());
        return trunkFeign.add(announcementVO);
    }

    /**
     *
     * 功能描述: 订单数据导入
     *
     * @param
     * @return
     * @auther wzc
     * @date 2018/12/24 16:13
     */
    @PostMapping("importExcel")
    public R<Boolean> importOrder(@RequestParam MultipartFile file) throws Exception {
        if(file.isEmpty()){
            return new R<>(Boolean.FALSE,"上传文件为空，请重新上传！");
        }
        return ordOrderService.importOrder(file,UserUtils.getUser(),getTenantId());
    }

    @GetMapping("/downloadCnImportModel")
    @ApiOperation(value = "下载集装箱订单导入模板")
    public void downModel(HttpServletRequest request, HttpServletResponse response) {
        String filename = "集装箱订单导入模板.xls";
        String path = "static/excel/集装箱订单导入模板(新版).xls";
        CommonUtils.downloadThymeleaf(resourceLoader,filename,path,request,response);
    }

    /**
     *
     * 功能描述: 订单数据导出
     *
     * @param
     * @return
     * @auther wzc
     * @date 2018/12/24 16:13
     */
    @GetMapping("exportExcel")
    public boolean exportOrder(HttpServletResponse response, OrderSearch ordOrder){
        ordOrder.setTenantId(getTenantId());
        return ordOrderService.exportOrder(response, ordOrder);
    }

    /*********************************************/
    /******************小程序接口******************/
    /*********************************************/

    /**
     *
     * 功能描述: 小程序端根据订单编号进行查询
     *
     * @param orderId
     * @return com.zhkj.lc.order.model.entity.OrdOrder
     * @auther wzc
     * @date 2019/1/3 16:02
     */
    @GetMapping("selectByOrderIdForApp/{orderId}")
    public OrdOrder getForApp(@PathVariable String orderId) {
        return ordOrderService.selectOrderByOrderIdForApp(orderId, getTenantId());
    }

    /**
     *
     * 功能描述: 订单派遣司机
     *
     * @param orderId 订单编号
     * @param driverId 司机id
     * @return boolean
     * @auther wzc
     * @date 2018/12/25 10:22
     */
    @GetMapping("sendDriver")
    @Transactional
    public R<Boolean> sendDriver(String orderId, Integer driverId, String plateNumber) throws IOException {
        if(orderId == null || driverId == null){
            return new R<>(Boolean.FALSE);
        }
        /*给司机派单前验证*/
        if(orderId!=null){
            DriverVO driverVO = trunkFeign.selectDriverStatus(driverId, getTenantId());
            if(driverVO == null){
                return new R<>(Boolean.FALSE,"该司机不存在！");
            }else {
                if(!driverVO.getStatus().equals("2")){
                    return new R<>(Boolean.FALSE,driverVO.getDriverName()+"该司机处于请假状态或者在途状态，不能接单！");
                }
                if(driverVO.getDelFlag().equals("1")){
                    return new R<>(Boolean.FALSE,driverVO.getDriverName()+"该司机已被删除！");
                }
                if(!driverVO.getPlateNumber().equals(plateNumber)){
                    return new R<>(Boolean.FALSE,"该司机和车辆信息不匹配！");
                }
            }
        }
        //todo 短信发送接口调用
        DriverVO driverVO = new DriverVO();
        driverVO.setDriverId(driverId);
        List<DriverVO> driverVs = trunkFeign.selectAllDriver(driverVO);
        if(driverVs != null && driverVs.size() == 1){
            OrdOrder ordOrder = new OrdOrder();
            if (systemFeginServer.selectIsSend(getTenantId())) {
                SysSmsTempVO s = systemFeginServer.selectSmsSetByTplId(getTenantId(), CommonConstant.TPL_ID_NEW);
                if ((!s.getIsSend().equals("1")) && (!s.getIsSendDriver().equals("1"))) {
                    YunPianSMSUtils.sendDriverNewOrder(driverVs.get(0).getPhone(),orderId);
                }
            }
            ordOrder.setOrderId(orderId);
            ordOrder.setStatus(CommonConstant.ORDER_DJD);//订单状态已派车
            ordOrder.setSendTruckDate(new Date());//派车日期
            ordOrder.setUpdateBy(UserUtils.getUser());
            ordOrder.setUpdateTime(new Date());
            OrdOrder param = new OrdOrder();
            param.setOrderId(orderId);
            ordOrderService.update(ordOrder, new EntityWrapper<>(param));
            //查询订单信息
            OrdOrder  order = ordOrderService.selectOrderById(ordOrder.getOrderId(),getTenantId());
            /*上报gps、箱型亚欧系统司机车辆信息*/
            reportDriver(order);
            OrdOrderLogistics logistics = new OrdOrderLogistics();
            logistics.setLogisticsMsg("订单正在派车中");
            logistics.setOrderId(orderId);
            logistics.setOrderStatus(CommonConstant.ORDER_DJD);
            logistics.setTenantId(getTenantId());
            OrdOrderLogistics log = logisticsService.selectByOrderIdAndStatus(logistics.getOrderId(),CommonConstant.ORDER_DJD);
            if(log==null){
                logisticsService.insertOrderLogistics(logistics);
            }else {
                logistics.setId(log.getId());
                logisticsService.updateById(logistics);
            }
            /*查询目前车辆下的司机信息*/
            List<DriverVO> status = trunkFeign.selectDriverStatusByPlateNumber(plateNumber);
            Integer[] driverIds =  new Integer[status.size()];
            for(int i =0 ; i<status.size();i++){
                driverIds[i] = status.get(i).getDriverId();
            }
            driverVO.setStatus(CommonConstant.SJZT);
            driverVO.setDriverIds(driverIds);
            commonGoodsTruckFeign.updateDriverSta(driverVO);
            return new R<>(Boolean.TRUE,"订单派车成功！");
        }else{
            return new R<>(Boolean.FALSE,"订单派车失败！");
        }
    }

    /**
     *
     * 功能描述: 查询所有司机（有车辆）
     *
     * @return DriverVOs
     * @auther wzc
     * @date 2018/12/27 17:48
     */
    @GetMapping("driverList")
    @ApiOperation(value = "功能描述: 查询所有司机（有车辆）")
    public List<DriverVO> selectDriverList(){
        DriverVO driverVO = new DriverVO();
        driverVO.setTenantId(getTenantId());
        driverVO.setIsTrust("0");
        driverVO.setStatus(CommonConstant.SJKX);
        return trunkFeign.getDriverList(driverVO);
    }

	@GetMapping("testDriver")
	@ApiOperation(value = "测试查询司机")
	public List<DriverVO> testDriver(DriverVO driverVO){

		return trunkFeign.getDriverList(driverVO);
	}


	/**
     *
     * 功能描述: 查询所有客户
     *
     * @return CustomerVOs
     * @auther wzc
     * @date 2018/12/27 17:48
     */
    @GetMapping("customerList")
    public List<CustomerVO> selectCustomerList(){
        CustomerVO customerVO = new CustomerVO();
        customerVO.setTenantId(getTenantId());
        return trunkFeign.getCustomerList(customerVO);
    }

    /**
     *
     * 功能描述: 查询所有承运商
     *
     * @return CustomerVOs
     * @auther wzc
     * @date 2018/12/27 17:48
     */
    @GetMapping("truckTeamList")
    public List<TruckTeamVo>selectTruckTeamList(){
        TruckTeamVo teamVo = new TruckTeamVo();
        teamVo.setTenantId(getTenantId());
        teamVo.setIsTrust("0");
        return trunkFeign.getTruckTeams(teamVo);
    }

    /**
     *
     * 功能描述: 根据司机id查询司机信息
     *
     * @param driverId	司机id
     * @return 司机信息
     * @auther wzc
     * @date 2018/12/27 17:51
     */
    @GetMapping("driver/{driverId}")
    public DriverVO selectDriverById(@PathVariable Integer driverId){
        DriverVO driverVO = new DriverVO();
        driverVO.setTenantId(getTenantId());
        driverVO.setDriverId(driverId);
        driverVO.setStatus(CommonConstant.SJKX);
        List<DriverVO> drivers = trunkFeign.getDriverList(driverVO);
        if(drivers!=null && drivers.size()==1){
            if(drivers.get(0) != null && drivers.get(0).getPlateId() != null){
                TruckVO truckVO = new TruckVO();
                truckVO.setTruckId(drivers.get(0).getPlateId());
                truckVO.setTenantId(getTenantId());
                List<TruckVO> truckVOs = trunkFeign.selectTruckList(truckVO);
                if(truckVOs!=null && truckVOs.size()==1){
                    drivers.get(0).setPlateNumber(truckVOs.get(0).getPlateNumber());
                }else{
                    drivers.get(0).setPlateNumber("");
                }
            }
            return drivers.get(0);
        }
        return null;
    }

    /**
     *
     * 功能描述: 根据客户id（货代）查询客户（货代）信息
     *
     * @param customerId 客户id
     * @return 客户（货代）信息
     * @auther wzc
     * @date 2018/12/27 17:51
     */
    @GetMapping("customer/{customerId}")
    public CustomerVO selectCustomerById(@PathVariable Integer customerId){
        CustomerVO customerVO = new CustomerVO();
        customerVO.setTenantId(getTenantId());
        customerVO.setCustomerId(customerId);
        List<CustomerVO> customerVOS = trunkFeign.selectAllForFegin(customerVO);
        if(customerVOS!=null && customerVOS.size()==1){
            return customerVOS.get(0);
        }
        return null;
    }

    @ApiOperation(value = "查询所有非删除客户")
    @GetMapping("allCustomer")
    public List<CustomerVO> selectAllCustomer(){
        CustomerVO customerVO = new CustomerVO();
        customerVO.setTenantId(getTenantId());
        customerVO.setDelFlag(CommonConstant.STATUS_NORMAL);
        return trunkFeign.selectAllForFegin(customerVO);
    }

    /**
     *
     * 功能描述: 距离计算接口
     *
     * @param origin	起始地址
     * @param destination	终点地址
     * @return float
     * @auther wzc
     * @date 2019/1/10 17:16
     */
    @GetMapping("getDistances")
    public R<Float> getDistances(String origin, String destination){
        String dis = null;
        try {
            dis = BaiDuMapUtils.getDistance(origin, destination);
        }catch (Exception e){
            return new R<>(null,"计算失败！");
        }
        if(dis.contains("公里")){
            dis = dis.replace("公里","");
            return new R<>(Float.valueOf(dis));
        }
        return new R<>(Float.valueOf(dis.replace("米",""))/1000);
    }

    @ApiOperation(value = "百度地图输入提示")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "query",value = "输入地址"),
            @ApiImplicitParam(name = "region",value = "限制城市")
    })
    @GetMapping("cityLimit")
    public List<CityAddress.ResultDetail>cityLimit(String region, String query){
        if(StringUtils.isEmpty(query))return null;
        String ak = "XoWiKlCuvwBwX1GFIo84zF4hQW3P88l8";
        String resultJson = HttpClientUtil
                .doGet("http://api.map.baidu.com/place/v2/suggestion?query="+query+"&region="+region+"&city_limit=false&output=json&ak="+ak);
        //GSON直接解析成对象
        CityAddress resultBean = new Gson().fromJson(resultJson,CityAddress.class);
        //对象中拿到集合
        List<CityAddress.ResultDetail> BeanList = resultBean.getResult();
        for(CityAddress.ResultDetail detail : BeanList){
            detail.setName(detail.getProvince()+detail.getCity()+detail.getDistrict()+detail.getName());
        }
        return BeanList;
    }

    /**
     *
     * 功能描述: 根据司机id查询当前运输的订单号
     *
     * @param driverId
     * @param tenantId
     * @return java.lang.String
     * @auther wzc
     * @date 2019/2/23 15:01
     */
    @GetMapping("selectByDriverIdForOil")
    public String selectByDriverIdForOil(Integer driverId ,Integer tenantId){
        OrdOrder ordOrder = new OrdOrder();
        ordOrder.setTenantId(tenantId);
        ordOrder.setDriverId(driverId);
        OrdCommonGoods commonOrder = new OrdCommonGoods();
        commonOrder.setDriverId(driverId);
        commonOrder.setMtenantId(tenantId);
        String orderId = null;
        try {
            List<OrderSearch> order = ordOrderService.selectOrderByDriverIdFeign(ordOrder);
            if(order != null && order.size() > 0){
                orderId = order.get(0).getOrderId();
            }
            List<CommonOrdSearch> commonGoods = commonGoodsService.selectOrderByDriverIdFeign(commonOrder);
            if(commonGoods != null && commonGoods.size() > 0){
                orderId = commonGoods.get(0).getOrderId();
            }
        }catch (Exception e){
        }
        return orderId;
    }

    /**
     *
     * 功能描述: 根据司机id查询当前月订单数
     *
     * @param driverId
     * @param tenantId
     * @return java.lang.Integer
     * @auther wzc
     * @date 2019/2/25 15:11
     */
    @GetMapping("selectCountByDriver")
    public Integer selectCountByDriver(Integer driverId ,Integer tenantId){
        /*普货订单数*/
        int CN_count = ordOrderService.selectCountByDriver(driverId, tenantId, getFirstDate(), getLastDate());

        /*普货订单数*/
        int PH_count = commonGoodsService.selectCountByDriver(driverId, tenantId, getFirstDate(), getLastDate());
        return CN_count + PH_count;
    }

    /**
     *
     * 功能描述: 查询gps运踪轨迹信息
     *
     * @param orderId
     * @return java.util.List<java.lang.String>
     * @auther wzc
     * @date 2019/2/27 9:12
     */
    @GetMapping("orderGPS/{orderId}")
    public Map getOrderGPS(@PathVariable String orderId){
        return ordOrderService.getGPSList(orderId, getTenantId());
    }

    public int reportDriver(OrdOrder order){
        String orderId = order.getOrderId();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(order.getUpstreamId()!=null && order.getDriverId()!=null && order.getPlateNumber() != null){
            DriverVO driverVO = new DriverVO();
            driverVO.setTenantId(order.getTenantId());
            driverVO.setDriverId(order.getDriverId());
            DriverVO driver = trunkFeign.selectAllDriver(driverVO).get(0);
            if (StringUtils.isNotEmpty(order.getUpstreamId())){
                UpstreamOrderDriver orderDriver = new UpstreamOrderDriver();
                orderDriver.setUpstreamId(order.getUpstreamId());
                orderDriver.setCarrier(order.getCarrier());
                orderDriver.setTenantId(order.getTenantId());
                orderDriver.setRsoPlateNum(order.getPlateNumber());
                orderDriver.setRsoTownerName(driver.getDriverName());
                orderDriver.setRsoTownerPhone(driver.getPhone());
                orderDriver.setRsoTruckType(driver.getIdcardNumber());
                orderDriver.setRsoContainerNum(order.getContainerNo());
                orderDriver.setRsoSealNum(null);
                Gson gson = new Gson();
                String json = gson.toJson(orderDriver);

                //gps接口
                GpsOrder gpsOrder = new GpsOrder();
                gpsOrder.setUuid(orderId);
                gpsOrder.setCompname(order.getCarrier());
                gpsOrder.setDrivername(order.getDriverName());
                gpsOrder.setDriverphone(order.getDriverPhone());
                gpsOrder.setPlatenum(order.getPlateNumber());
                //司机信息是必须的吗idCard
                gpsOrder.setIdCard(driver.getIdcardNumber());
                gpsOrder.setGocome(order.getType());//01去，1回
                gpsOrder.setBoxnum(order.getContainerNo());
                gpsOrder.setBoxtype(order.getContainerType());
                gpsOrder.setProxynum(order.getClassOrder());//委托书编号
                gpsOrder.setGoodname(order.getProductName());//货物名
                if(order.getType().equals("0")){
                    //去程
                    gpsOrder.setDestination("陆港");
                    gpsOrder.setTopickupgoods(order.getPickupGoodsPlace());
                }else{
                    //回程
                    gpsOrder.setDestination(order.getSendGoodsPlace());//卸货地和送货地是同一个字段吗
                }
                //接单时间如何确认,还没写
                gpsOrder.setReturnboxadress(order.getReturnConPlace());//还箱地
                gpsOrder.setContainerNum(order.getContainerNum());//箱量
                gpsOrder.setSealNumber(order.getSealNumber());//铅封号
                gpsOrder.setWeight(order.getWeight());//重量
                gpsOrder.setSize(order.getSize());//体积
                gpsOrder.setConsignor(order.getConsignor());//发货人
                gpsOrder.setConsignorPhone(order.getConsignorPhone()); //发货人联系方式
                gpsOrder.setConsignee(order.getConsignee());//收货人
                gpsOrder.setConsigneePhone(order.getConsigneePhone()); //收货人联系方式
                gpsOrder.setKilometre(order.getKilometre());//公里数
                gpsOrder.setClassDate(sf.format(order.getClassDate()));//班列日期
                String json1 = gson.toJson(gpsOrder);
                try {

                     HttpClientUtil.doPostJson("http://xxt.zhonghaokeji.cn/coc/PC/updateCCMWhenCar.do",json);//箱型亚欧线上地址
                    // System.out.println("paiche---xxyo"+json);
                   // HttpClientUtil.doPostJson("http://p5rpqr.natappfree.cc/coc/PC/updateCCMWhenCar.do",json);//箱型亚欧线下映射地址
                    // System.out.println("paiche---gps"+gpsOrder);
                    HttpClientUtil.doPostJson("http://171.15.132.161:8081/RoadGPS/pc/carSelect/getDataFromBox.do",json1);//GPS线上映射地址
                    // System.out.println("paiche---"+gpsOrder);
                }catch (Exception e){
                    System.out.println(order.getOrderId()+"上报公路系统失败！");
                    return 2;
                }
                return 1;
            }
        }
        return 0;
    }

    @ApiOperation(value = "短信重发接口")
    @ApiImplicitParam("orderId-订单编号，phone-发送对象手机号，sort-提送货地址id，plateNumber-车牌号")
    @GetMapping("smsSendAgain")
    public R<Boolean> smsSendAgain(String orderId, String phone, Integer sort, String plateNumber){
        return ordOrderService.smsSendAgain(orderId, getTenantId(), phone, sort, plateNumber);
    }

    @ApiOperation(value = "获取当前开票税率接口")
    @GetMapping("getRate")
    public BigDecimal getRate(){
        return ordOrderService.getRate(getTenantId());
    }
    /**
     * 获取自由车辆
     */
    @ApiOperation(value = "选择空闲车辆集合")
    @PostMapping("/getAllEnableTruck")
    public List<TruckVO> getZYTruckList() {
        TruckVO truckVO = new TruckVO();
        truckVO.setTenantId(getTenantId());
        truckVO.setIsTrust("0");
        List<TruckVO> list = commonGoodsTruckFeign.getZYTruckList(truckVO);
        //System.out.println("list===="+list);
        // 正在进行中的订单
        List<Integer> listPlateNumber = commonGoodsService.selectTruckIdByProc(getTenantId());

        if (null != listPlateNumber && listPlateNumber.size() > 0) {
            if (null != list && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    if (listPlateNumber.contains(list.get(i).getTruckId())){
                        list.remove(i);
                        i--;
                    }
                }
            }
        }
        return list;
    }

}
