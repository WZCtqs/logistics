package com.zhkj.lc.order.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zhkj.lc.common.api.YunPianSMSUtils;
import com.zhkj.lc.common.constant.CommonConstant;
import com.zhkj.lc.common.util.CommonUtils;
import com.zhkj.lc.common.util.R;
import com.zhkj.lc.common.vo.DriverVO;
import com.zhkj.lc.order.dto.OrdCommonGoodsForApp;
import com.zhkj.lc.order.dto.PhOrdForUpd;
import com.zhkj.lc.order.dto.SysSmsTempVO;
import com.zhkj.lc.order.feign.CommonGoodsTruckFeign;
import com.zhkj.lc.order.feign.SystemFeginServer;
import com.zhkj.lc.order.feign.TrunkFeign;
import com.zhkj.lc.order.mapper.*;
import com.zhkj.lc.order.model.entity.OrdOrder;
import com.zhkj.lc.order.model.entity.OrdOrderLogistics;
import com.zhkj.lc.order.model.entity.OrdPickupArrivalAdd;
import com.zhkj.lc.order.model.entity.OrderSettlement;
import com.zhkj.lc.order.service.IOrdCommonGoodsService;
import com.zhkj.lc.order.service.IOrdOrderLogisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 运输跟踪 服务实现类
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
@Service
public class OrdOrderLogisticsServiceImpl extends ServiceImpl<OrdOrderLogisticsMapper, OrdOrderLogistics> implements IOrdOrderLogisticsService {

    @Autowired
    private OrdOrderLogisticsMapper ordOrderLogisticsMapper;

    @Autowired
    private OrdOrderServiceImpl orderService;

    @Autowired
    private OrdCommonGoodsMapper commonGoodsMapper;

    @Autowired
    private OrdOrderMapper ordOrderMapper;

    @Autowired
    private CommonGoodsTruckFeign commonGoodsTruckFeign;

    @Autowired
    private TrunkFeign trunkFeign;

    @Autowired
    private IOrdCommonGoodsService commonGoodsService;

    @Autowired
    private SystemFeginServer systemFeginServer;
    @Autowired
    private OrdPickupArrivalAddMapper addMapper;
    @Autowired
    private OrderSettlementMapper settlementMapper;

    /**
     * 查询运输跟踪列表
     *
     * @param orderId 运输跟踪信息
     * @return 运输跟踪集合
     */
    @Override
    public List<OrdOrderLogistics> selectOrderList(String orderId) {
        return ordOrderLogisticsMapper.selectOrderLogisticsList(orderId);
    }

    @Override
    public List<OrdOrderLogistics> selectOrderListByTenantId(OrdOrderLogistics ordOrderLogistics) {
        return ordOrderLogisticsMapper.selectOrderListByTenantId(ordOrderLogistics);
    }

    /**
     * 新增运输跟踪
     *
     * @param orderLogistics 运输跟踪信息
     * @return 结果
     */
    @Override
    public boolean insertOrderLogistics(OrdOrderLogistics orderLogistics) {
        return ordOrderLogisticsMapper.insertOrderLogistics(orderLogistics);
    }

    /**
     * 修改运输跟踪
     *
     * @param orderLogistics 运输跟踪信息
     * @return 结果
     */
    @Override
    public boolean updateOrderLogistics(OrdOrderLogistics orderLogistics) {
        return ordOrderLogisticsMapper.updateOrderLogistics(orderLogistics);
    }

    /**
     * 删除运输跟踪对象
     *
     * @param id 需要删除的数据ID
     * @return 结果
     */
    @Override
    public boolean deleteOrderLogisticsById(Integer id) {
        return ordOrderLogisticsMapper.deleteOrderLogisticsById(id);
    }

    /**
     * 功能描述: 司机接单接口
     *
     * @param orderId 订单编号
     * @return com.zhkj.lc.common.util.R<java.lang.Boolean>
     * @auther wzc
     * @date 2019/1/16 14:02
     */
    @Override
    @Transactional
    public R<Boolean> driverReceipt(String orderId, String logisticsMsg, Integer tenantId) {
        String orderType = orderId.substring(0, 2);
        /*司机姓名*/
        String driverName = null;
        /*车牌号*/
        String plateNumber = null;
        /*司机手机号*/
        String driverPhone = null;
        /*订单状态*/
        String stauts = null;

        switch (orderType) {
            case "CN":
                OrdOrder ordOrder = orderService.selectOrderById(orderId,tenantId);
                if (ordOrder != null) {
                    stauts = ordOrder.getType().equals("0") ? CommonConstant.ORDER_DTX : CommonConstant.ORDER_DTH;
                    ordOrder.setOrderId(orderId);
                    ordOrder.setStatus(stauts);
                    orderService.updateOrderStatus(ordOrder);
                    driverName = ordOrder.getDriverName();
                    driverPhone = ordOrder.getDriverPhone();
                    plateNumber = ordOrder.getPlateNumber();
                    /*如果开启发送功能*/
                    boolean isSend = false;
                    if(systemFeginServer.selectIsSend(tenantId)) isSend = true;
                    SysSmsTempVO s = systemFeginServer.selectSmsSetByTplId(tenantId, CommonConstant.TPL_ID_RCODE);
                    if(s != null && s.getIsSendReceice().equals("0")){
                        isSend = true;
                    }else {
                        isSend = false;
                    }
                    /*发送短信给收货人*/
                    List<OrdPickupArrivalAdd> arrs = ordOrder.getArrivalAdds();
                    for (OrdPickupArrivalAdd add : arrs) {
                        /*发送结果默认false*/
                        boolean sendresult = false;
                        String receiptCode = String.valueOf(CommonUtils.getRandom(6));
                        if(isSend){ /*已设置发送功能*/
                            if(add.getContactsPhone().length() == 11) { //手机号长度为11位
                                try {
                                    R<Boolean> reslut = YunPianSMSUtils.sendConsigneeReceiptCode(add.getContactsPhone(), receiptCode, orderId, add.getSort(), plateNumber);
                                    sendresult = reslut.getData();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        add.setReceiptCode(receiptCode);
                        add.setIsSendOk(sendresult?"1":"0");
                        addMapper.updateById(add);
                    }
                }
                break;
            case "PH":
                stauts = CommonConstant.ORDER_DTH;//待提货状态
                PhOrdForUpd phOrdForUpd = commonGoodsService.selectUpdOrdForApp(orderId);
                if (phOrdForUpd != null) {
                    phOrdForUpd.setOrderId(orderId);
                    phOrdForUpd.setStatus(stauts);
                    phOrdForUpd.setIsSend("1");
                    commonGoodsMapper.updateCommonOrdAPP(phOrdForUpd);
                    driverName = phOrdForUpd.getDriverVO().getDriverName();
                    driverPhone = phOrdForUpd.getDriverVO().getPhone();
                    plateNumber = phOrdForUpd.getPlateNumber();
                    /*如果开启发送功能*/
                    boolean isSend = false;
                    if(systemFeginServer.selectIsSend(tenantId)) isSend = true;
                    SysSmsTempVO s = systemFeginServer.selectSmsSetByTplId(tenantId, CommonConstant.TPL_ID_RCODE);
                    if(s != null && s.getIsSendReceice().equals("0")){
                        isSend = true;
                    }else {
                        isSend = false;
                    }
                    /*发送短信给收货人*/
                    List<OrdPickupArrivalAdd> arrs = phOrdForUpd.getArrivalAdds();
                    for (OrdPickupArrivalAdd add : arrs) {
                        /*发送结果默认false*/
                        boolean sendresult = false;
                        String receiptCode = String.valueOf(CommonUtils.getRandom(6));
                        if(isSend){ /*已设置发送功能*/
                            if(add.getContactsPhone().length() == 11) { //手机号长度为11位
                                try {
                                    R<Boolean> reslut = YunPianSMSUtils.sendConsigneeReceiptCode(add.getContactsPhone(), receiptCode, orderId, add.getSort(), plateNumber);
                                    sendresult = reslut.getData();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        add.setReceiptCode(receiptCode);
                        add.setIsSendOk(sendresult?"1":"0");
                        addMapper.updateById(add);
                    }
                }
                break;
        }
        /*更新运踪信息*/
        OrdOrderLogistics logistics = new OrdOrderLogistics();
        logistics.setLogisticsMsg("订单派车成功，车牌号：" + plateNumber + "司机姓名：" + driverName + ",联系方式：" + driverPhone);
        logistics.setOrderId(orderId);
        logistics.setLogisticsAddress(logisticsMsg);
        logistics.setOrderStatus(stauts);
        logistics.setTenantId(tenantId);
        OrdOrderLogistics log = ordOrderLogisticsMapper.selectByOrderIdAndStatus(orderId,stauts);
        if(log==null){
            ordOrderLogisticsMapper.insertOrderLogistics(logistics);
        }else {
            logistics.setId(log.getId());
            ordOrderLogisticsMapper.updateById(logistics);
        }
        return new R<>(Boolean.TRUE);
    }

    /**
     * 功能描述: 集装箱——司机提箱中接口
     *
     * @param orderId 订单编号
     * @return com.zhkj.lc.common.util.R<java.lang.Boolean>
     * @auther wzc
     * @date 2019/1/16 14:15
     */
    @Override
    public R<Boolean> pickCn(String orderId, String logisticsMsg, Integer tenantId) {
        OrdOrder ordOrder = new OrdOrder();
        ordOrder.setOrderId(orderId);
        ordOrder.setStatus(CommonConstant.ORDER_TXZ);//司机提箱中

        OrdOrderLogistics logistics = new OrdOrderLogistics();
        logistics.setOrderId(orderId);
        logistics.setLogisticsAddress(logisticsMsg);
        logistics.setOrderStatus(CommonConstant.ORDER_TXZ);
        logistics.setTenantId(tenantId);
        logistics.setLogisticsMsg("司机正在提箱途中");

        OrdOrderLogistics log = ordOrderLogisticsMapper.selectByOrderIdAndStatus(orderId,CommonConstant.ORDER_TXZ);
        if(log==null){
            ordOrderLogisticsMapper.insertOrderLogistics(logistics);
        }else {
            logistics.setId(log.getId());
            ordOrderLogisticsMapper.updateById(logistics);
        }
        orderService.updateOrderStatus(ordOrder);
        return new R<>(Boolean.TRUE);
    }

    /**
     * 功能描述: 提箱完成
     *
     * @param logistics
     * @return com.zhkj.lc.common.util.R<java.lang.Boolean>
     * @auther wzc
     * @date 2019/1/16 14:26
     */
    @Override
    public R<Boolean> pickCned(OrdOrderLogistics logistics, Integer tenantId) {
        OrdOrder ordOrder = new OrdOrder();
        ordOrder.setOrderId(logistics.getOrderId());
        ordOrder.setContainerNo(logistics.getContainerNo());
        ordOrder.setSealNumber(logistics.getSealNumber());
        ordOrder.setStatus(CommonConstant.ORDER_DTH);//司机提箱完成-待提货状态
        /*更新运踪信息*/
        logistics.setLogisticsAddress(logistics.getLogisticsMsg());
        logistics.setLogisticsMsg("已提箱，准备提货");
        logistics.setOrderStatus(CommonConstant.ORDER_DTH);
        logistics.setTenantId(tenantId);

        OrdOrderLogistics log = ordOrderLogisticsMapper.selectByOrderIdAndStatus(logistics.getOrderId(),CommonConstant.ORDER_DTH);
        if(log==null){
            ordOrderLogisticsMapper.insertOrderLogistics(logistics);
        }else {
            logistics.setId(log.getId());
            ordOrderLogisticsMapper.updateById(logistics);
        }

        orderService.updateOrderStatus(ordOrder);
        return new R<>(Boolean.TRUE);
    }

    /**
     * 功能描述: 提货中
     *
     * @param orderId
     * @return com.zhkj.lc.common.util.R<java.lang.Boolean>
     * @auther wzc
     * @date 2019/1/16 14:30
     */
    @Override
    public R<Boolean> pickupGoods(String orderId, String logisticsMsg, Integer tenantId) {
        String orderType = orderId.substring(0, 2);
        switch (orderType) {
            case "CN":
                OrdOrder ordOrder = orderService.selectOrderById(orderId, tenantId);
                if (ordOrder != null) {
                    ordOrder.setOrderId(orderId);
                    ordOrder.setStatus(CommonConstant.ORDER_THZ);//提货中状态
                    orderService.updateOrderStatus(ordOrder);
                }
                break;
            case "PH":
                PhOrdForUpd phOrdForUpd = commonGoodsService.selectUpdOrdForApp(orderId);
                if (phOrdForUpd != null) {
                    phOrdForUpd.setOrderId(orderId);
                    phOrdForUpd.setStatus(CommonConstant.ORDER_THZ);
                    commonGoodsMapper.updateCommonOrdAPP(phOrdForUpd);

                }
                break;
        }
        /*更新运踪信息*/
        OrdOrderLogistics logistics = new OrdOrderLogistics();
        logistics.setLogisticsMsg("正在努力赶往提货地提货");
        logistics.setOrderStatus(CommonConstant.ORDER_THZ);
        logistics.setOrderId(orderId);
        logistics.setLogisticsAddress(logisticsMsg);
        logistics.setTenantId(tenantId);
        OrdOrderLogistics log = ordOrderLogisticsMapper.selectByOrderIdAndStatus(orderId,CommonConstant.ORDER_THZ);
        if(log==null){
            ordOrderLogisticsMapper.insertOrderLogistics(logistics);
        }else {
            logistics.setId(log.getId());
            ordOrderLogisticsMapper.updateById(logistics);
        }
        return new R<>(Boolean.TRUE);
    }

    /**
     * 功能描述: 提货完成，上传提货凭证
     *
     * @param logistics
     * @return com.zhkj.lc.common.util.R<java.lang.Boolean>
     * @auther wzc
     * @date 2019/1/16 14:37
     */
    @Override
    public R<Boolean> pickupGoodsed(OrdOrderLogistics logistics, Integer tenantId) {
        String orderType = logistics.getOrderId().substring(0, 2);
        String status = null;
        /*订单号*/
        String orderId = null;
        switch (orderType) {
            case "CN":
                OrdOrder ordOrder = orderService.selectOrderById(logistics.getOrderId(), tenantId);
                if (ordOrder != null) {
                    orderId = ordOrder.getOrderId();
                    ordOrder.setOrderId(logistics.getOrderId());
                    if(ordOrder.getType().equals("1")){
                        ordOrder.setSealNumber(logistics.getSealNumber());
                    }
                    if(isEnd(ordOrder.getPickupAdds())){ //如果全部提货 状态为运输中，否则 提货中
                        status = CommonConstant.ORDER_YSZ;
                    }else {
                        status = CommonConstant.ORDER_THZ;
                    }
                    ordOrder.setStatus(status);
                    orderService.updateOrderStatus(ordOrder);
                }
                break;
            case "PH":
                PhOrdForUpd phOrdForUpd = commonGoodsService.selectUpdOrdForApp(logistics.getOrderId());
                if (phOrdForUpd != null) {
                    orderId = phOrdForUpd.getOrderId();
                    phOrdForUpd.setOrderId(logistics.getOrderId());

                    if(isEnd(phOrdForUpd.getPickupAdds())){ //如果全部提货 状态为运输中，否则 提货中
                        status = CommonConstant.ORDER_YSZ;
                    }else {
                        status = CommonConstant.ORDER_THZ;
                    }
                    phOrdForUpd.setStatus(status);
                    commonGoodsMapper.updateCommonOrdAPP(phOrdForUpd);
                }
                break;
        }
        /*更新运踪信息*/
        logistics.setOrderStatus(status);
        logistics.setLogisticsAddress(logistics.getLogisticsMsg());
        logistics.setLogisticsMsg("提货已完成");
        logistics.setTenantId(tenantId);
        OrdOrderLogistics log = ordOrderLogisticsMapper.selectByOrderIdAndStatus(orderId,status);
        if(log==null){
            ordOrderLogisticsMapper.insertOrderLogistics(logistics);
        }else {
            logistics.setId(log.getId());
            ordOrderLogisticsMapper.updateById(logistics);
        }
        /*更新地址数据*/
        OrdPickupArrivalAdd add = new OrdPickupArrivalAdd();
        add.setId(logistics.getAddId());
        add.setSuccessTime(new Date());
        add.setSuccessAdd(logistics.getLogisticsAddress());
        add.setFiles(logistics.getPhotos());
        addMapper.updateById(add);
        return new R<>(Boolean.TRUE);
    }

//    public boolean sendGoodsOwnr(Integer tenantId, String phoneS, String phoneF, String receiptCode, String orderId){
//        R<Boolean> resultS = new R<>(Boolean.FALSE);
////        R<Boolean> resultF = new R<>(Boolean.FALSE);;
//        try {
//            if(systemFeginServer.selectIsSend(tenantId)){
//                SysSmsTempVO s = systemFeginServer.selectSmsSetByTplId(tenantId, CommonConstant.TPL_ID_RCODE);
//                if(s!=null){
//                    if(s.getIsSendReceice().equals("0")){
//                        resultS = YunPianSMSUtils.sendConsigneeReceiptCode(phoneS, receiptCode, orderId, 1,"a");
//                    }
//                    if(s.getIsSendPicker().equals("0")){
//                        YunPianSMSUtils.sendConsigneeReceiptCode(phoneF, receiptCode, orderId, 1,"a");
//                    }
//                }
//            }
//        }catch (Exception e){
//            return false;
//        }
//        if(resultS.getData()){
//            return true;
//        }
//        return false;
//    }

    /**
     * 功能描述: 签收中
     *
     * @param orderId
     * @return com.zhkj.lc.common.util.R<java.lang.Boolean>
     * @auther wzc
     * @date 2019/1/16 18:06
     */
    @Override
    public R<Boolean> receipting(String orderId,String logisticsMsg, Integer tenantId) {
        String orderType = orderId.substring(0, 2);
        switch (orderType) {
            case "CN":
                OrdOrder ordOrder = new OrdOrder();
                ordOrder.setOrderId(orderId);
                ordOrder.setStatus(CommonConstant.ORDER_DQS);//签收中状态
                orderService.updateOrderStatus(ordOrder);
                break;
            case "PH":
                PhOrdForUpd phOrdForUpd = commonGoodsService.selectUpdOrdForApp(orderId);
                if (phOrdForUpd != null) {
                    phOrdForUpd.setOrderId(orderId);
                    phOrdForUpd.setStatus(CommonConstant.ORDER_DQS);
                    commonGoodsMapper.updateCommonOrdAPP(phOrdForUpd);
                }
                break;
        }
        /*更新运踪信息*/
        OrdOrderLogistics logistics = new OrdOrderLogistics();
        logistics.setOrderId(orderId);
        logistics.setLogisticsAddress(logisticsMsg);
        logistics.setTenantId(tenantId);
        logistics.setOrderStatus(CommonConstant.ORDER_DQS);
        logistics.setLogisticsMsg("货品待签收");
        OrdOrderLogistics log = ordOrderLogisticsMapper.selectByOrderIdAndStatus(logistics.getOrderId(),CommonConstant.ORDER_DQS);
        if(log==null){
            ordOrderLogisticsMapper.insertOrderLogistics(logistics);
        }else {
            logistics.setId(log.getId());
            ordOrderLogisticsMapper.updateById(logistics);
        }
        return new R<>(Boolean.TRUE);
    }

    /**
     * 功能描述: 已签收
     *
     * @param logistics
     * @return com.zhkj.lc.common.util.R<java.lang.Boolean>
     * @auther wzc
     * @date 2019/1/16 18:06
     */
    @Override
    public R<Boolean> receipted(OrdOrderLogistics logistics) {
        logistics.setLogisticsMsg("货品已签收");
        /*更新运踪信息*/
        logistics.setOrderStatus(CommonConstant.ORDER_YQS);
        ordOrderLogisticsMapper.insertOrderLogistics(logistics);
        return new R<>(Boolean.TRUE);
    }

    /**
     * 功能描述: 集装箱——司机提箱中接口
     *
     * @param orderId 订单编号
     * @return com.zhkj.lc.common.util.R<java.lang.Boolean>
     * @auther wzc
     * @date 2019/1/16 14:15
     */
    @Override
    public R<Boolean> returnCn(String orderId, String logisticsMsg, Integer tenantId) {
        OrdOrder ordOrder = new OrdOrder();
        ordOrder.setOrderId(orderId);
        ordOrder.setStatus(CommonConstant.ORDER_DHX);//司机还箱中

        OrdOrderLogistics logistics = new OrdOrderLogistics();
        logistics.setOrderId(orderId);
        logistics.setLogisticsAddress(logisticsMsg);
        logistics.setTenantId(tenantId);
        logistics.setOrderStatus(CommonConstant.ORDER_DHX);
        logistics.setLogisticsMsg("司机正在还箱途中");
        OrdOrderLogistics log = ordOrderLogisticsMapper.selectByOrderIdAndStatus(logistics.getOrderId(),CommonConstant.ORDER_DHX);
        if(log==null){
            ordOrderLogisticsMapper.insertOrderLogistics(logistics);
        }else {
            logistics.setId(log.getId());
            ordOrderLogisticsMapper.updateById(logistics);
        }
        orderService.updateOrderStatus(ordOrder);
        return new R<>(Boolean.TRUE);
    }

    /**
     * 功能描述: 提箱完成
     *
     * @param logistics
     * @return com.zhkj.lc.common.util.R<java.lang.Boolean>
     * @auther wzc
     * @date 2019/1/16 14:26
     */
    @Override
    public R<Boolean> returnCned(OrdOrderLogistics logistics, Integer tenantId) {
        OrdOrder ordOrder = new OrdOrder();
        ordOrder.setOrderId(logistics.getOrderId());
        ordOrder.setStatus(CommonConstant.ORDER_YHX);//司机还箱完成-
        /*更新运踪信息*/
        logistics.setTenantId(tenantId);
        logistics.setLogisticsAddress(logistics.getLogisticsMsg());
        logistics.setLogisticsMsg("已还箱");
        logistics.setOrderStatus(CommonConstant.ORDER_YHX);
        OrdOrderLogistics log = ordOrderLogisticsMapper.selectByOrderIdAndStatus(logistics.getOrderId(),CommonConstant.ORDER_YHX);
        if(log==null){
            ordOrderLogisticsMapper.insertOrderLogistics(logistics);
        }else {
            logistics.setId(log.getId());
            ordOrderLogisticsMapper.updateById(logistics);
        }
        orderService.updateOrderStatus(ordOrder);
        /*新增结算状态*/
        OrderSettlement settlement = settlementMapper.selectSettlementByOrderId(ordOrder.getOrderId());
        if(settlement == null){
            settlement = new OrderSettlement();
            settlement.setOrderId(ordOrder.getOrderId());
            settlementMapper.insert(settlement);
        }
        return new R<>(Boolean.TRUE);
    }

    /**
     * 判断服务码是否存在,更新订单状态
     *
     * @param receiptCode
     * @return
     */
    @Override
    @Transactional
    public Boolean hasReceiptCode(String receiptCode, String orderId,Integer driverId,Integer tenantId, Integer addId) {
        OrdPickupArrivalAdd add = new OrdPickupArrivalAdd();
        add.setId(addId);
        add.setReceiptCode(receiptCode);
        add.setOrderId(orderId);
        add = addMapper.selectOne(add);
        /*是否是最后一个*/
        boolean isEnd = false;
        List<OrdPickupArrivalAdd> arrivalAdds = addMapper.selectArrivalByOrderId(orderId);
        if(isEnd(arrivalAdds)){
            isEnd = true;
        }
        if(add == null){
            return false;
        }else {
            /*验证码正确，更新状态*/
            add.setSuccessTime(new Date());
            addMapper.updateById(add);
        }
        String orderType = orderId.substring(0, 2);
        switch (orderType) {
            case "CN":
                if(isEnd) {
                    OrdOrder order = ordOrderMapper.selectOrderBaseById(orderId, tenantId);
                    OrdOrder ordOrder = new OrdOrder();
                    if (order != null) {
                        if (order.getType().equals("0")) { //去程
                            ordOrder.setStatus(CommonConstant.ORDER_YHX);// 11 已还箱
                            DriverVO driverVO = new DriverVO();
                            driverVO.setStatus(CommonConstant.SJKX); //司机空闲
                            driverVO.setDriverId(driverId);
                            /*查询司机的车牌号*/
                            DriverVO driver = trunkFeign.selectDriverStatus(driverId, tenantId);
                            /*查询目前车辆下的司机信息*/
                            List<DriverVO> status = trunkFeign.selectDriverStatusByPlateNumber(driver.getPlateNumber());
                            Integer[] driverIds = new Integer[status.size()];
                            for (int i = 0; i < status.size(); i++) {
                                driverIds[i] = status.get(i).getDriverId();
                            }
                            driverVO.setDriverIds(driverIds);
                            commonGoodsTruckFeign.updateDriverSta(driverVO);//更改司机状态
                            /*新增结算数据*/
                            OrderSettlement settlement = settlementMapper.selectSettlementByOrderId(orderId);
                            if(settlement == null){
                                settlement = new OrderSettlement();
                                settlement.setOrderId(orderId);
                                settlementMapper.insert(settlement);
                            }
                        } else {
                            ordOrder.setStatus(CommonConstant.ORDER_YQS);// 9 已签收
                        }
                    } else {
                        return Boolean.FALSE;
                    }
                    ordOrder.setOrderId(orderId);
                    orderService.updateOrderStatus(ordOrder);//修改订单状态
                    return Boolean.TRUE;
                }
                return true;
            case "PH":
                if(isEnd) {
                    PhOrdForUpd phOrdForUpd = new PhOrdForUpd();
                    phOrdForUpd.setOrderId(orderId);
                    phOrdForUpd.setStatus(CommonConstant.ORDER_YQS);
                    commonGoodsMapper.updateCommonOrdAPP(phOrdForUpd);
                    OrdCommonGoodsForApp ogApp = commonGoodsService.selectDetailByOrderId(orderId, tenantId);
                    Integer[] driverIds;
                    //普货签收时更新司机信息
                    if (ogApp.getOrdCommonTruck().getMdriverId() != null && ogApp.getOrdCommonTruck().getSdriverId() != null) {
                        driverIds = new Integer[2];
                        driverIds[0] = ogApp.getOrdCommonTruck().getMdriverId();
                        driverIds[1] = ogApp.getOrdCommonTruck().getSdriverId();
                    } else {
//                        driverIds = new Integer[1];
//                        driverIds[0] = ogApp.getOrdCommonTruck().getMdriverId();
                        /*查询司机的车牌号*/
                        DriverVO driver = trunkFeign.selectDriverStatus(driverId, tenantId);
                        /*查询目前车辆下的司机信息*/
                        List<DriverVO> status = trunkFeign.selectDriverStatusByPlateNumber(driver.getPlateNumber());
                        driverIds = new Integer[status.size()];
                        for (int i = 0; i < status.size(); i++) {
                            driverIds[i] = status.get(i).getDriverId();
                        }
                    }
                    DriverVO driverVO = new DriverVO();
                    driverVO.setStatus(CommonConstant.SJKX);
                    driverVO.setDriverIds(driverIds);
                    commonGoodsTruckFeign.updateDriverSta(driverVO);
                    OrderSettlement settlement = settlementMapper.selectSettlementByOrderId(orderId);
                    if(settlement == null){
                        settlement = new OrderSettlement();
                        settlement.setOrderId(orderId);
                        settlementMapper.insert(settlement);
                    }
                    return Boolean.TRUE;
                }
                return true;
        }
        return Boolean.FALSE;
    }

    @Override
    public Integer hasqsInfo(String orderId) {
        return ordOrderLogisticsMapper.hasqsInfo(orderId);
    }

    @Override
    public int countOrders(Integer tenantId) {
        return ordOrderLogisticsMapper.countOrders(tenantId);
    }

    @Override
    public int countAllOrders(Integer tenantId) {
        return ordOrderLogisticsMapper.countAllOrders(tenantId);
    }

    @Override
    public OrdOrderLogistics selectFirstOrderLogistics(String orderId,String orderStatus) {
        return ordOrderLogisticsMapper.selectFirstOrderLogistics(orderId,orderStatus);
    }

    @Override
    public OrdOrderLogistics selectLastOrderLogistics(String orderId,String orderStatus) {
        return ordOrderLogisticsMapper.selectLastOrderLogistics(orderId,orderStatus);
    }

    @Override
    public OrdOrderLogistics selectByOrderIdAndStatus(String orderId,String orderStatus) {
        return ordOrderLogisticsMapper.selectByOrderIdAndStatus(orderId,orderStatus);
    }

    /*
     * 判断是否还有未操作的地址（未提货、未送货）
     * 返回 true 已结束  false未结束
     **/
    public boolean isEnd(List<OrdPickupArrivalAdd> list){
        boolean b = false;
        int i = 0;
        for(OrdPickupArrivalAdd add : list){
            if(add.getSuccessTime()==null){
               i ++;
            }
        }
        if( i == 1) b = true;
        return b;
    }

}
