package com.zhkj.lc.order.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import com.zhkj.lc.common.constant.CommonConstant;
import com.zhkj.lc.common.util.UserUtils;
import com.zhkj.lc.common.vo.*;
import com.zhkj.lc.order.dto.CommonOrdSearch;
import com.zhkj.lc.order.dto.OrdCommonGoodsVo;
import com.zhkj.lc.order.dto.SysAnnouncementDto;
import com.zhkj.lc.order.feign.CommonGoodsTruckFeign;
import com.zhkj.lc.order.feign.SystemFeginServer;
import com.zhkj.lc.order.feign.TrunkFeign;
import com.zhkj.lc.order.model.entity.OrdPickupArrivalAdd;
import com.zhkj.lc.order.service.IOrdOrderService;
import com.zhkj.lc.order.utils.CommonUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.plugins.Page;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.R;
import com.zhkj.lc.order.model.entity.OrdCommonGoods;
import com.zhkj.lc.order.service.IOrdCommonGoodsService;
import com.zhkj.lc.common.web.BaseController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author cb
 * @since 2019-01-03
 */
@RestController
@RequestMapping("/ordCommonGoods")
public class OrdCommonGoodsController extends BaseController {
    @Autowired
    private IOrdCommonGoodsService ordCommonGoodsService;

    @Autowired
    private CommonGoodsTruckFeign commonGoodsTruckFeign;

    @Autowired private TrunkFeign trunkFeign;

    @Autowired private IOrdOrderService ordOrderService;


    @Resource
    private ResourceLoader resourceLoader;

    /**
     * 通过ID查询
     *
     * @param id ID
     * @return OrdCommonGoods
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据订单的id获取单条详细数据")
    public R<OrdCommonGoodsVo> get(@PathVariable(value = "id") Integer id) {
        OrdCommonGoodsVo ordCommonGoods = ordCommonGoodsService.selectComOrdById(id,getTenantId());
        return new R<>(ordCommonGoods);
    }
//--------------------------------------

    /**
     * 订单管理分页查询草稿
     *
     * @param params
     * @param commonOrdSearch
     * @return
     */
    @GetMapping("/managePage")
    @ApiOperation(value = "订单管理分页查询草稿", notes = "")
    public Page getManageList(@RequestParam Map<String, Object> params, CommonOrdSearch commonOrdSearch) {
        commonOrdSearch.setTenantId(getTenantId());
        return ordCommonGoodsService.selectManageGoodsList(new Query<>(params), commonOrdSearch);
    }

    //------------------------------------
    /**
     * 添加
     *
     * @param ordCommonGoods 实体
     * @return success/false
     */
    @Transactional
    @PostMapping
    @ApiOperation(value = "添加普货订单", notes = "")
    public R<Boolean> add(@RequestBody OrdCommonGoods ordCommonGoods) {
        ordCommonGoods.setMtenantId(getTenantId());
        ordCommonGoods.setCreateBy(UserUtils.getUser());
        BigDecimal rate = ordOrderService.getRate(getTenantId());
        if(rate == null){
            return new R<>(Boolean.FALSE,"请先进行税率设置！");
        }
        ordCommonGoods.getOrdCommonTruck().setPayRate(rate);
        return new R<>(ordCommonGoodsService.addCommonOrder(ordCommonGoods));
    }
//-----------------------------------------
    /**
     * 导入普货订单数据
     *
     * @param file
     * @return
     */

    @PostMapping("/importPHOrd")
    @ApiOperation(value = "普货订单导入")
    public R<Boolean> importOrd(@RequestParam MultipartFile file) {
        return ordCommonGoodsService.importPhOrd(file,UserUtils.getUser(),getTenantId());
    }
    //----------------------------------

    /**
     * 删除
     *
     * @param map
     * @return success/false
     */
    @DeleteMapping("/deleteBatches")
    @ApiOperation(value = "批量删除普货订单数据")
    public R<Boolean> delete(@RequestBody Map<Integer, String> map) {

        return new R<>(ordCommonGoodsService.deleteByIds(map, getTenantId(), UserUtils.getUser()));
    }
//--------------------------------------
    /**
     * 编辑
     *
     * @param ordCommonGoods 实体
     * @return success/false
     */
    @PutMapping
    @ApiOperation(value = "更新普货订单")
    public R<Boolean> edit(@RequestBody OrdCommonGoods ordCommonGoods) {
        if(ordCommonGoods.getOrdCommonTruck().getPayRate() == null){
            BigDecimal rate = ordOrderService.getRate(getTenantId());
            if(rate == null){
                return new R<>(Boolean.FALSE,"请先进行税率设置！");
            }
            ordCommonGoods.getOrdCommonTruck().setPayRate(rate);
        }
        return new R<>(ordCommonGoodsService.updatePhOrd(ordCommonGoods,getTenantId()));
    }
    @PutMapping("/addAnno")
    @ApiOperation(value = "新增功能 -- 更新已发送司机的普货订单 同时在小程序公告模块进行通知")
    public R<Boolean> edit2(@RequestBody OrdCommonGoods ordCommonGoods) {
        edit(ordCommonGoods);
        AnnouncementVO announcementVO=new AnnouncementVO ();
        announcementVO.setTitle("订单信息修改");
        announcementVO.setContent("订单编号为"+ordCommonGoods.getMorderId()+"的普货订单已发生修改，请查看详细信息");
        announcementVO.setDriverOwerId(ordCommonGoods.getDriverId());
        announcementVO.setType("0");
        announcementVO.setTenantId(getTenantId());
        announcementVO.setCreateBy(UserUtils.getUser());
        return trunkFeign.add(announcementVO);
    }
//-------------------------------------------

    @PostMapping("/sendDriverWhenAdding")
    @ApiOperation(value = "在新增订单时就发送普货订单给司机")
    public R<Boolean> sendDriver(@RequestBody OrdCommonGoods ordCommonGoods) {
        BigDecimal rate = ordOrderService.getRate(getTenantId());
        if(rate == null){
            return new R<>(Boolean.FALSE,"请先进行税率设置！");
        }
        ordCommonGoods.getOrdCommonTruck().setPayRate(rate);
        return ordCommonGoodsService.sendDriverWhenAdding(ordCommonGoods,getTenantId());
    }
//--------------------------------------------
    @PutMapping("/sendDriverInManagePage/{id}")
    @ApiOperation(value = "在订单管理界面进行发送司机的操作")
    public R<Boolean> sendDriverAtManage(@PathVariable Integer id) {
        OrdCommonGoodsVo ordCommonGoods = ordCommonGoodsService.selectComOrdById(id,getTenantId());
        return ordCommonGoodsService.sendDriverAtManagePage(ordCommonGoods);
    }
//---------------------------------------------

    @GetMapping("/ExportPHOrd")
    @ApiOperation(value = "导出普货订单草稿")
    public Boolean exportord(HttpServletRequest request, HttpServletResponse response, String ids) {
        return ordCommonGoodsService.exportPhOrd(request, response, ids, getTenantId());
    }
//-------------------------------------
    @GetMapping("/centerPage")
    @ApiOperation(value = "普货订单中心分页查询")
    public Page centerpage(@RequestParam Map<String, Object> params, CommonOrdSearch commonOrdSearch) {
        commonOrdSearch.setTenantId(getTenantId());
        return ordCommonGoodsService.selectCenterPage(new Query<>(params), commonOrdSearch);
    }
    //-----------------------------------

    @GetMapping("/ExportPHCenter")
    @ApiOperation(value = "导出普货订单中心数据")
    public Boolean exportCenter(HttpServletRequest request, HttpServletResponse response, String ids) {
        return ordCommonGoodsService.exportPHCenter(request, response, ids, getTenantId());
    }
    //--------------------------------------

    @GetMapping("/downloadPhImportModel")
    @ApiOperation(value = "下载普货导入模板")
    public void downModel(HttpServletRequest request, HttpServletResponse response) {
        String filename = "普货订单导入模板.xlsx";
        String path = "static/excel/普货订单导入模板(新版).xlsx";
        CommonUtils.downloadThymeleaf(resourceLoader,filename,path,request,response);
    }
    //--------------------------------------

    /*********************************************/
    /******************feign接口*****************/
    /*********************************************/

    @PostMapping("/getCustomer")
    @ApiOperation(value = "调用车辆服务获取客户信息")
    public List<CustomerVO> getCustomerInfo() {
        CustomerVO customerVO = new CustomerVO();
        customerVO.setIsTrust("0");
        customerVO.setTenantId(getTenantId());
        return commonGoodsTruckFeign.getCustomerForPh(customerVO);
    }

    /**
     * 获取在途车辆id
     */
    @ApiOperation(value = "外调接口：获取在途车辆id", notes = "tenantId租户id")
    @GetMapping("/getPlateNumberByProc/{tenantId}")
    public List<DriverVO> selectPlateNumberByProc(@PathVariable("tenantId") Integer tenantId) {
        return ordCommonGoodsService.selectPlateNumberByProc(tenantId);
    }

    /**
     * 获取自由车辆
     */
    @ApiOperation(value = "选择自有车类型的车辆集合", notes = "attribute是车辆类型，自有车是0")
    @PostMapping("/getZYTruck/{attribute}")
    public List<TruckVO> getZYTruckList(@PathVariable("attribute") String attribute) {
        TruckVO truckVO = new TruckVO();
        truckVO.setAttribute(attribute);
        truckVO.setTenantId(getTenantId());
        truckVO.setIsTrust("0");
        List<TruckVO> list = commonGoodsTruckFeign.getZYTruckList(truckVO);
        // 正在进行中的订单
        List<Integer> listPlateNumber = ordCommonGoodsService.selectTruckIdByProc(getTenantId());
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

    /**
     * 根据当前选择的车辆信息获取对应的司机信息
     */
    @PostMapping("/getDriverByPlateId/{plateId}")
    @ApiOperation(value = "根据车辆id查询下属司机信息集合")
    public List<DriverVO> getDriverByPlateId(@PathVariable Integer plateId) {
        // 非请假的司机（在途和空闲的）
        DriverVO driverVO = new DriverVO();
        driverVO.setStatus(CommonConstant.SJKX);
        driverVO.setPlateId(plateId);
        driverVO.setTenantId(getTenantId());
        List<DriverVO> list = commonGoodsTruckFeign.getDriverListByPlateId(driverVO);
        driverVO.setStatus(CommonConstant.SJZT);
        List<DriverVO> list1 = commonGoodsTruckFeign.getDriverListByPlateId(driverVO);
        list.addAll(list1);
        return list;
    }

    /**
     * 获取所有外调车的车队信息（承运商）
     */

    @PostMapping("/getTruckTeam")
    @ApiOperation(value = "查询所有承运商集合")
    public List<TruckTeamVo> getTruckTeam() {
        TruckTeamVo truckTeamVo = new TruckTeamVo();
        truckTeamVo.setIsTrust("0");
        truckTeamVo.setTenantId(getTenantId());
        return commonGoodsTruckFeign.getTruckTeamList(truckTeamVo);
    }

    /**
     * 根据选择的车队获取旗下的车辆信息
     */

    @PostMapping("/getTruckListByTeamId/{truckTeamId}")
    @ApiOperation(value = "根据车队id查询下属所有车辆集合")
    public List<TruckVO> getTruckListByTeamId(@PathVariable Integer truckTeamId) {
        TruckVO truckVO = new TruckVO();
        truckVO.setTenantId(getTenantId());
        truckVO.setTruckTeamId(truckTeamId);
        List<TruckVO> truckVOS = commonGoodsTruckFeign.getTruckListByTeamId(truckVO);
        List<TruckVO> returnTrucks = new ArrayList<>();
        for (TruckVO t : truckVOS){

            if(Integer.valueOf(trunkFeign.checkTruckDriverStatus(t.getPlateNumber(),getTenantId())) == 0 && !t.getAttribute().equals("0")) {
                returnTrucks.add(t);
            }
        }
        return returnTrucks;
    }


    /**
     *
     * 功能描述: 查询所有司机（有车辆）
     *
     * @return DriverVOs
     * @auther wzc
     * @date 2018/12/27 17:48
     */
    @GetMapping("/allDriver")
    public List<DriverVO>selectDriverList(){
        DriverVO driverVO = new DriverVO();
        driverVO.setTenantId(getTenantId());
        return trunkFeign.getDriverList(driverVO);
    }

    @GetMapping("/allTruck")
    public List<TruckVO> selectTruckList(){
        TruckVO truckVO = new TruckVO();
        truckVO.setTenantId(getTenantId());
        return trunkFeign.selectTruckList(truckVO);
    }

    @GetMapping("/allTruTruckOwn")
    public List<TruTruckOwnVo> allTruTruckOwn(){
        TruTruckOwnVo truTruckOwnVo=new TruTruckOwnVo();
        truTruckOwnVo.setTenantId(getTenantId());
        return trunkFeign.getAllTruTruckOwn(truTruckOwnVo);
    }

    @ApiOperation(value = "根据司机ids和租户id查询正在进行中的订单的数量")
    @GetMapping("/selectProcOrdByDriverIds")
    public Integer selectProcOrdByDriverIds(@RequestParam("driverIds") Integer[] driverIds, @RequestParam("tenantId") Integer tenantId) {
        Integer countResult = ordCommonGoodsService.selectProcOrdByDriverIds(driverIds, tenantId);
        return countResult;
    }

}
