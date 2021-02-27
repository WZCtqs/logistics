package com.zhkj.lc.order.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zhkj.lc.common.constant.CommonConstant;
import com.zhkj.lc.common.util.AjaxResult;
import com.zhkj.lc.common.util.DateUtils;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.R;
import com.zhkj.lc.common.util.excel.ExcelUtil;
import com.zhkj.lc.common.util.support.Convert;
import com.zhkj.lc.common.vo.DriverVO;
import com.zhkj.lc.common.vo.SysDictVO;
import com.zhkj.lc.common.vo.TruckVO;
import com.zhkj.lc.order.dto.*;
import com.zhkj.lc.order.feign.SystemFeginServer;
import com.zhkj.lc.order.feign.TrunkFeign;
import com.zhkj.lc.order.mapper.OrdShortOrderMapper;
import com.zhkj.lc.order.model.entity.OrdShortOrder;
import com.zhkj.lc.order.model.entity.ShortOrderBill;
import com.zhkj.lc.order.service.IOrdShortOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 盘短管理信息 服务实现类
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
@Service
public class OrdShortOrderServiceImpl extends ServiceImpl<OrdShortOrderMapper, OrdShortOrder> implements IOrdShortOrderService {
    @Autowired
    private OrdShortOrderMapper shortOrderMapper;

    @Autowired
    private TrunkFeign truckFegin;

    @Autowired
    private SystemFeginServer systemFeginServer;

    private static  final String PERFIX = "PD";
    /**
     * 查询盘短管理信息
     *
     * @param orderId 盘短管理ID
     * @return 盘短管理信息
     */
    @Override
    public OrdShortOrder selectShortOrderByOrderId(String orderId) {
        return shortOrderMapper.selectShortOrderByOrderId(orderId);
    }

    /**
     * 查询盘短管理列表
     *
     * @param shortOrder 盘短管理信息
     * @return 盘短匹配管理集合
     */
    @Override
    public Page<OrdShortOrder> selectShortOrderList(Query query, ShortSearch shortOrder) {
        Integer tenantId = shortOrder.getTenantId();
        List<OrdShortOrder> ordShortOrders = shortOrderMapper.selectShortOrderList(query, shortOrder);
//        List<SysDictVO>lines = systemFeginServer.findDictByType("short_order_line", tenantId);
        List<SysDictVO>types = systemFeginServer.findDictByType("short_order_type", tenantId);
        ordShortOrders.forEach(order -> {
//            order.setDriverName(getDriverName(order.getDriverId(),tenantId));
            order.setShortType(getLabel(types,order.getShortType()));
//            order.setTransLine(getLabel(lines, order.getTransLine()));
        });
        return query.setRecords(ordShortOrders);
    }

    /**
     * 功能描述: 导入应付应收盘短订单
     *
     * @param file 应付应收excel文件
     * @return ajax 返回结果信息
     * @auther wzc
     * @date 2018/12/12 16:58
     */
    @Override
    @Transactional
    public R<Boolean> importShortOrder(MultipartFile file, String loginName, Integer tenantId) throws Exception {

        InputStream inputStream = file.getInputStream();
        StringBuilder msg = new StringBuilder();//返回提示消息
        try {
            ImportParams params = new ImportParams();
            params.setHeadRows(1);
            params.setTitleRows(1);
            List<ShortOrderExportDTO> shortOrders = ExcelImportUtil.importExcel(inputStream, ShortOrderExportDTO.class, params);
            List<OrdShortOrder> inserts = new ArrayList<>();
            if(shortOrders.size() > 0) {
                //获取所有盘短订单的路线集合以便进行录入验证
                List<SysDictVO> types = systemFeginServer.findDictByType("short_order_type", tenantId);
                for (int i = 0; i < shortOrders.size(); i++) {
                    ShortOrderExportDTO order = shortOrders.get(i);
                    if(order==null)continue;
                    try {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        String orderTime = DateUtils.CTS_DateFormatString(order.getOrderTime());
                        String date = format.format(order.getOrderDate());
                        Date orderDateTime = DateUtils.DateFormat(date + " " +orderTime.substring(10));
                        order.setOrderDate(orderDateTime);
                    }catch (Exception e){
                        continue;
                    }
//                    /*司机验证*/
//                    DriverVO driver = getDriverByName(order.getDriverName(),tenantId);
//                    if (driver == null) {
//                        msg.append("司机姓名“" + order.getDriverName() + "”系统中不存在！");
//                        return new R<>(Boolean.FALSE, msg.toString());
//                    }
                    /*车牌号验证*/
//                    String plate = getTruckByPlateNumber(order.getPlateNumber(),tenantId);
//                    if (plate == null) {
//                        msg.append("车牌号“" + order.getPlateNumber() + "”系统中不存在！");
//                        return new R<>(Boolean.FALSE, msg.toString());
//                    }
                    /*盘短类型验证*/
                    String type = getValue(types, order.getShortType());
                    if (type == null) {
                        msg.append("业务类型“" + order.getShortType() + "”系统中不存在！");
                        return new R<>(Boolean.FALSE, msg.toString());
                    }
                    order.setShortType(type);
//                    order.setDriverId(driver.getDriverId());
                    OrdShortOrder shortOrder = new OrdShortOrder();
                    BeanUtils.copyProperties(order, shortOrder);
                    inserts.add(shortOrder);
                }
                inserts.forEach(so->{
                    so.setCreateBy(loginName);
                    so.setTenantId(tenantId);
                    insertShortOrder(so);
                });
            }
        }catch (ParseException e){
            return new R<>(Boolean.FALSE,"解析失败，请检查导入文件数据格式！");
        }
        inputStream.close();
        return new R<>(Boolean.TRUE);
    }

    /**
     * 功能描述: 导出应付应收盘短订单
     *
     * @param shortSearch 查询条件
     * @return 导出结果信息
     * @auther wzc
     * @date 2019/01/08 10:37
     */
    @Override
    public R<Boolean> exportShortOrder(HttpServletResponse response, ShortSearch shortSearch) throws Exception {
        Integer tenantId = shortSearch.getTenantId();
        try {
            if(shortSearch.getIds() != null && shortSearch.getIds() != ""){
                shortSearch.setOrderIds(Convert.toStrArray(shortSearch.getIds()));
            }
//            List<SysDictVO> lines = systemFeginServer.findDictByType("short_order_line",shortSearch.getTenantId());
            List<SysDictVO> types = systemFeginServer.findDictByType("short_order_type",shortSearch.getTenantId());
            List<OrdShortOrderForExport> shortOrders = shortOrderMapper.selectShortOrderListForExport(shortSearch);
            shortOrders.forEach(order -> {
//                order.setDriverName(getDriverName(order.getDriverId(),tenantId));
                order.setShortType(getLabel(types, order.getShortType()));
//                order.setTransLine(getLabel(lines, order.getTransLine()));
            });
            //导出excel文件名：“导出日期” + “-” + 盘短类型 + “应付盘短信息”
            DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            String excelName = fmt.format(new Date()) + "-盘短订单信息";
            ExcelUtil<OrdShortOrderForExport> utilshortSanHuos = new ExcelUtil<>(OrdShortOrderForExport.class);
            utilshortSanHuos.exportExcel(response, shortOrders, excelName, null);
        }catch (Exception e){
            return new R<>(Boolean.FALSE,e.getMessage());
        }
        return new R<>(Boolean.TRUE);
    }

    //获取字典lable
    public String getLabel(List<SysDictVO>dicts, String value){
        if(value==null ||("").equals(value)){
            return null;
        }
        for (SysDictVO type:dicts) {
            if(value.equals(type.getValue())){
                return type.getLabel();
            }
        }
        return null;
    }

    //获取字典value
    public String getValue(List<SysDictVO>dicts,String lable){
        if(lable==null ||("").equals(lable)){
            return null;
        }
        for (SysDictVO line:dicts) {
            if(lable.equals(line.getLabel())){
                return line.getValue();
            }
        }
        return null;
    }

    //获取司机姓名
    public String getDriverName(Integer driverId, Integer tenantId){
        if (driverId != null) {
            DriverVO driverVO = new DriverVO();
            driverVO.setDriverId(driverId);
            driverVO.setTenantId(tenantId);
            List<DriverVO> driverVOs = truckFegin.selectAllDriver(driverVO);
            if(driverVOs != null && driverVOs.size() ==1){
                return driverVOs.get(0).getDriverName();
            }
            return "";
        }else{
            return "";
        }
    }
    //根据司机姓名获取信息
    public DriverVO getDriverByName(String driverName, Integer tenantId){
        if (driverName == null || ("").equals(driverName)) {
            return null;
        }
        DriverVO driverVO = new DriverVO();
        driverVO.setDriverName(driverName);
        driverVO.setTenantId(tenantId);
        List<DriverVO> driverVOS = truckFegin.selectAllDriver(driverVO);
        if(driverVOS != null && driverVOS.size() == 1){
            return driverVOS.get(0);
        }
        return null;
    }
    //根据车牌号获取信息
    public String getTruckByPlateNumber(String plateNumber, Integer tenantId){
        if (plateNumber == null || ("").equals(plateNumber)) {
            return null;
        }
        TruckVO truckVO = new TruckVO();
        truckVO.setTenantId(tenantId);
        truckVO.setPlateNumber(plateNumber);
        List<TruckVO> truckVOS = truckFegin.selectTruckList(truckVO);
        if(truckVOS != null && truckVOS.size() == 1) {
            return truckVOS.get(0).getPlateNumber();
        }
        return null;
    }

    /**
     * 新增盘短管理
     *
     * @param shortOrder 盘短管理信息
     * @return 结果
     */
    @Override
    public boolean insertShortOrder(OrdShortOrder shortOrder) {
        shortOrder.setOrderId(getOrderId());
        return shortOrderMapper.insertShortOrder(shortOrder);
    }

    /**
     * 修改盘短管理
     *
     * @param shortOrder 盘短管理信息
     * @return 结果
     */
    @Override
    public boolean updateShortOrder(OrdShortOrder shortOrder) {
        return shortOrderMapper.updateShortOrder(shortOrder);
    }

    /**
     * 删除盘短管理对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public boolean deleteShortOrderByIds(String ids, String loginName) {
        try{
            shortOrderMapper.deleteShortOrderByIds(CommonConstant.STATUS_DEL, loginName, Convert.toStrArray(ids));
        }catch (Exception e){
            return false;
        }
        return true;
    }

    @Override
    public int countShortOrders(Integer tenantId) {
        return shortOrderMapper.countShortOrders(tenantId);
    }

    @Override
    public Boolean updateOrderBalanceStatus(OrdShortOrder shortOrder) {
        return shortOrderMapper.updateOrderBalanceStatus(shortOrder);
    }

    @Override
    public List<Integer> countOrderNumber(Integer tenantId) {
        return shortOrderMapper.countOrderNumber(tenantId);
    }

    @Override
    public List<BigDecimal> countMoney(Integer tenantId) {
        return shortOrderMapper.countMoney(tenantId);
    }

    public String getOrderId(){
        Map map = shortOrderMapper.getOrderId(PERFIX,"");
        System.out.println(map.get("orderId"));
        if(map.get("orderId")==null){
            throw new RuntimeException();
        }
        return map.get("orderId").toString();
    }
}
