package com.zhkj.lc.order.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.zhkj.lc.common.constant.CommonConstant;
import com.zhkj.lc.common.util.R;
import com.zhkj.lc.common.util.StringUtils;
import com.zhkj.lc.common.util.UserUtils;
import com.zhkj.lc.order.dto.PhOrdForUpd;
import com.zhkj.lc.order.mapper.*;
import com.zhkj.lc.order.model.entity.*;
import com.zhkj.lc.order.service.IOrdOrderLogisticsService;
import com.zhkj.lc.order.service.IOrdOrderService;
import com.zhkj.lc.order.service.IOrdPickupArrivalAdd;
import com.zhkj.lc.order.service.OrdAdditionalRecordingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class OrdAdditionalRecordingServiceImpl implements OrdAdditionalRecordingService{
    @Autowired
    private IOrdOrderService ordOrderService;
    @Autowired private IOrdOrderLogisticsService logisticsService;

    @Autowired
    private IOrdPickupArrivalAdd arrivalAdd;

    @Autowired
    private OrdOrderLogisticsMapper logisticsMapper;

    @Autowired private OrdPickupArrivalAddMapper addMapper;

    @Autowired
    private OrdOrderMapper ordOrderMapper;

    @Autowired
    private OrdCommonGoodsMapper ordCommonGoodsMapper;

    @Autowired
    private OrdCommonTruckMapper ordCommonTruckMapper;

    @Autowired
    private CommonGoodsInfoMapper commonGoodsInfoMapper;
    @Autowired
    private OrderSettlementMapper settlementMapper;

    @Override
    @Transactional
    public R ordAdditionalRecording(OrdOrder ordOrder, Integer tenantId) throws IOException {
        //创建订单－addAndSend－start
        BigDecimal rate = ordOrderService.getRate(tenantId);
        if(rate==null){
            return new R<>(Boolean.FALSE, "请先进行税率设置！");
        }
        ordOrder.setPayRate(rate);
        String orderId = ordOrder.getOrderId();
        ordOrder.setTenantId(tenantId);
        ordOrder.setSendTruckDate(new Date());//派车日期
        ordOrder.setStatus(CommonConstant.ORDER_DJD);
        /*给司机派单前验证*/
/*        DriverVO driverVO = new DriverVO();
        if(ordOrder.getDriverId()!=null){
            driverVO = trunkFeign.selectDriverStatus(ordOrder.getDriverId(), ordOrder.getTenantId());
            if(driverVO == null){
                return new R<>(Boolean.FALSE,"该司机不存在！");
            }
            else {
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
*/
        if(orderId == null){
            ordOrder.setTenantId(tenantId);
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
            //reportDriver(ordOrder);
        }
/*        if (systemFeginServer.selectIsSend(tenantId)) {
            SysSmsTempVO s = systemFeginServer.selectSmsSetByTplId(tenantId, CommonConstant.TPL_ID_NEW);
            if ((!s.getIsSend().equals("1")) && (!s.getIsSendDriver().equals("1"))) {
                YunPianSMSUtils.sendDriverNewOrder(driverVO.getPhone(),orderId);
            }
        }*/

        OrdOrderLogistics logistics = new OrdOrderLogistics();
        logistics.setLogisticsMsg("订单正在派车中");
        logistics.setOrderId(orderId);
        logistics.setTenantId(tenantId);
        logistics.setOrderStatus(CommonConstant.ORDER_DJD);
        //更新司机信息
/*        List<DriverVO> status = trunkFeign.selectDriverStatusByPlateNumber(ordOrder.getPlateNumber());
        Integer[] driverIds =  new Integer[status.size()];
        for(int i =0 ; i<status.size();i++){
            driverIds[i] = status.get(i).getDriverId();
        }
        DriverVO driver = new DriverVO();
        driver.setStatus(CommonConstant.SJZT);
        driver.setDriverIds(driverIds);
        commonGoodsTruckFeign.updateDriverSta(driver);*/
        OrdOrderLogistics log = logisticsService.selectByOrderIdAndStatus(logistics.getOrderId(),CommonConstant.ORDER_DJD);
        if(log==null){
            logisticsService.insertOrderLogistics(logistics);
        }else {
            logistics.setId(log.getId());
            logisticsService.updateById(logistics);
        }
        //创建订单－addAndSend－end
        return ordAdditionalRecording(logistics, orderId, tenantId);
    }

    @Override
    @Transactional
    public R ordAdditionalRecordingPH(OrdCommonGoods ordCommonGoods, Integer tenantId) throws IOException {
        BigDecimal rate = ordOrderService.getRate(tenantId);
        if (rate == null) {
            return new R<>(Boolean.FALSE, "请先进行税率设置！");
        }
        ordCommonGoods.getOrdCommonTruck().setPayRate(rate);
        //ordCommonGoodsService.sendDriverWhenAdding(ordCommonGoods,getTenantId());

        ordCommonGoods.setUpdateTime(new Date());
        //更改订单状态为待接单
        ordCommonGoods.setStatus(CommonConstant.ORDER_DJD);
        //更新派车时间
        ordCommonGoods.setSendTruckDate(new Date());
        //订单编号为空，表示新增时发送司机
        try {
            Map map = ordCommonGoodsMapper.getPhOrderId("PH", "");
            ordCommonGoods.setMorderId(map.get("orderId").toString());
            //保存普货订单信息

            ordCommonGoods.setSendGoodsPlace(StringUtils.join(ordCommonGoods.getSendPlaceArray(), "/"));
            ordCommonGoods.setPickGoodsPlace(StringUtils.join(ordCommonGoods.getPickPlaceArray(), "/"));
            // ordCommonGoods.setShipperCity(StringUtils.join(ordCommonGoods.getShipperCityArray(), "/"));
            //ordCommonGoods.setPickerCity(StringUtils.join(ordCommonGoods.getPickerCityArray(), "/"));
            //保存主司机id
            Integer driverId = ordCommonGoods.getOrdCommonTruck().getMdriverId();
            ordCommonGoods.setDriverId(driverId);
            //租户id
            ordCommonGoods.setMtenantId(tenantId);
            ordCommonGoodsMapper.insertCommonOrder(ordCommonGoods);
            /*处理地址信息*/
            List<OrdPickupArrivalAdd> arrivalAdds = ordCommonGoods.getArrivalAdds();
            for (OrdPickupArrivalAdd add : arrivalAdds) {
                add.setTenantId(ordCommonGoods.getMtenantId());
                add.setOrderId(ordCommonGoods.getMorderId());
                if (add.getAddressCity().contains("市辖区")) {
                    String[] city = add.getAddressCity().split("/");
                    add.setAddressCity(add.getAddressCity().replace("市辖区", city[0]));
                }
                addMapper.insert(add);
            }
            List<OrdPickupArrivalAdd> pickupAdds = ordCommonGoods.getPickupAdds();
            for (OrdPickupArrivalAdd add : pickupAdds) {
                add.setTenantId(ordCommonGoods.getMtenantId());
                add.setOrderId(ordCommonGoods.getMorderId());
                if (add.getAddressCity().contains("市辖区")) {
                    String[] city = add.getAddressCity().split("/");
                    add.setAddressCity(add.getAddressCity().replace("市辖区", city[0]));
                }
                addMapper.insert(add);
            }
            //保存货物信息
            List<CommonGoodsInfo> commonGoodsInfos = ordCommonGoods.getCommonGoodsInfos();
            for (CommonGoodsInfo commonGoodsInfo : commonGoodsInfos
                    ) {
                //设置货物的订单编号
                commonGoodsInfo.setOrderId(map.get("orderId").toString());
                commonGoodsInfo.setTenantId(tenantId);
                commonGoodsInfoMapper.insert(commonGoodsInfo);
            }

            //保存车辆调度信息
            OrdCommonTruck ordCommonTruck = ordCommonGoods.getOrdCommonTruck();
            ordCommonTruck.setOrderId(map.get("orderId").toString());
            ordCommonTruck.setTenantId(tenantId);
//                ordCommonTruck.setChargedMileage(ordCommonGoods.getMchargedMileage());
            ordCommonTruckMapper.insert(ordCommonTruck);

            //保存文件信息
          /*  if (ordCommonGoods.getOrdCommonFile() != null) {
                OrdCommonFile ordCommonFile = ordCommonGoods.getOrdCommonFile();
                ordCommonFile.setOrderId(map.get("orderId").toString());
                ordCommonFile.setTenantId(tenantId);
                ordCommonFileMapper.insert(ordCommonFile);
            }*/
            //更新运踪信息
            OrdOrderLogistics ordOrderLogistics = new OrdOrderLogistics();
            ordOrderLogistics.setLogisticsMsg(CommonConstant.PC_MSG);
            ordOrderLogistics.setOrderId(map.get("orderId").toString());
            ordOrderLogistics.setOrderStatus(CommonConstant.ORDER_DJD);
            ordOrderLogistics.setTenantId(tenantId);

            logisticsMapper.insertOrderLogistics(ordOrderLogistics);

            return ordAdditionalRecording(ordOrderLogistics, ordCommonGoods.getMorderId(), tenantId);
        }catch (Exception e){
            e.printStackTrace();
            return new R(false);
        }
    }


    public R ordAdditionalRecording(OrdOrderLogistics logistics, String orderId, Integer tenantId) throws IOException {
        String orderType = orderId.substring(0, 2);
        //司机接单－start
        //更新订单状态，路线表
        //司机接单－end
        OrdOrder ordOrder = new OrdOrder();
        //提箱中-start
        if ("CN".equals(orderType)){//集装箱类型
            ordOrder.setOrderId(orderId);
            ordOrder.setStatus(CommonConstant.ORDER_TXZ);//司机提箱中

            logistics.setOrderId(ordOrder.getOrderId());
            logistics.setLogisticsAddress("logisticsMsg");
            logistics.setOrderStatus(CommonConstant.ORDER_TXZ);
            logistics.setTenantId(tenantId);
            logistics.setLogisticsMsg("司机正在提箱途中");

            OrdOrderLogistics log = logisticsMapper.selectByOrderIdAndStatus(ordOrder.getOrderId(),CommonConstant.ORDER_TXZ);
            if(log==null){
                logisticsMapper.insertOrderLogistics(logistics);
            }else {
                logistics.setId(log.getId());
                logisticsMapper.updateById(logistics);
            }
            //ordOrderService.updateOrderStatus(ordOrder);
            //提箱中-end
            //提箱完成-start
            logistics.setPhotos(StringUtils.join(logistics.getPaths(),","));
            logisticsService.pickCned(logistics,tenantId);
        }
        //提箱完成-end
        //提货中-start
        logisticsService.pickupGoods(orderId, "logisticsMsg", tenantId);
        //提货中-end
        //上传提货凭证-start
        System.out.println("gangkaishi"+logistics.getLogisticsMsg());
        logistics.setPhotos(StringUtils.join(logistics.getPaths(),","));
        logisticsService.pickupGoodsed(logistics,tenantId);
        //上传提货凭证-end
        //签收中-start
        R<Boolean> f = logisticsService.receipting(orderId, "logisticsMsg", tenantId);
        //签收中-end
        //上传签收凭证-start
        /*更新签收凭证*/
        List<OrdPickupArrivalAdd> ordPickupArrivalAdds = arrivalAdd.selectArrivalByOrderId(orderId);
        OrdPickupArrivalAdd add = new OrdPickupArrivalAdd();
        add.setId(ordPickupArrivalAdds.get(0).getId());
        add.setFiles(StringUtils.join(logistics.getPaths(),","));
        add.setSuccessAdd(logistics.getLogisticsMsg());
        arrivalAdd.updateById(add);

        logistics.setTenantId(tenantId);
        logistics.setLogisticsAddress(logistics.getLogisticsMsg());//有问题
        //判断订单编号对应的签收状态是否已存在运踪信息，然后执行更新或新增
        Integer exit_flag = logisticsService.hasqsInfo(logistics.getOrderId());//
        //已存在，执行更新
        if(exit_flag==null){
            logisticsService.receipted(logistics);
        }else if(exit_flag.intValue()==1){
            logisticsService.updateById(logistics);
        }
        //上传签收凭证-end
        //上传签收码，更新订单为签收状态-start
        hasReceiptCode(orderId, tenantId, logistics);
        //上传签收码，更新订单为签收状态-end
        //还箱-start
        if ("CN".equals(orderType)){//集装箱类型
            logisticsService.returnCn(orderId, "logisticsMsg",tenantId);
            logisticsService.returnCned(logistics,tenantId);
        }
        //还箱-end
        return new R<>(Boolean.TRUE);
    }

    private Boolean hasReceiptCode(String orderId,Integer tenantId, OrdOrderLogistics logistics) {
/*        OrdPickupArrivalAdd add = new OrdPickupArrivalAdd();
        add.setId(addId);
        add.setOrderId(orderId);
        add = addMapper.selectOne(add);
        *//*是否是最后一个*//*
        boolean isEnd = false;
        List<OrdPickupArrivalAdd> arrivalAdds = addMapper.selectArrivalByOrderId(orderId);
        if(isEnd(arrivalAdds)){
            isEnd = true;
        }*/
        /*验证码正确，更新状态*/
        OrdPickupArrivalAdd add = new OrdPickupArrivalAdd();
        boolean isEnd = true;
        add.setSuccessTime(new Date());
        //addMapper.updateById(add);
        addMapper.update(add, new EntityWrapper<OrdPickupArrivalAdd>().eq("order_id", orderId));
        String orderType = orderId.substring(0, 2);
        switch (orderType) {
            case "CN":
                if(isEnd) {
                    OrdOrder order = ordOrderMapper.selectOrderBaseById(orderId, tenantId);
                    OrdOrder ordOrder = new OrdOrder();
                    if (order != null) {
                        if (order.getType().equals("0")) { //去程
                            ordOrder.setStatus(CommonConstant.ORDER_YHX);
                            /*DriverVO driverVO = new DriverVO();
                            driverVO.setStatus(CommonConstant.SJKX);
                            driverVO.setDriverId(driverId);
                            *//*查询司机的车牌号*//*
                            DriverVO driver = trunkFeign.selectDriverStatus(driverId, tenantId);
                            *//*查询目前车辆下的司机信息*//*
                            List<DriverVO> status = trunkFeign.selectDriverStatusByPlateNumber(driver.getPlateNumber());
                            Integer[] driverIds = new Integer[status.size()];
                            for (int i = 0; i < status.size(); i++) {
                                driverIds[i] = status.get(i).getDriverId();
                            }
                            driverVO.setDriverIds(driverIds);
                            commonGoodsTruckFeign.updateDriverSta(driverVO);*/
                            /*新增结算数据*/
                            OrderSettlement settlement = settlementMapper.selectSettlementByOrderId(orderId);
                            if(settlement == null){
                                settlement = new OrderSettlement();
                                settlement.setOrderId(orderId);
                                settlementMapper.insert(settlement);
                            }
                        } else {
                            logisticsService.returnCned(logistics, tenantId);
                        }
                    } else {
                        return Boolean.FALSE;
                    }
                    ordOrder.setOrderId(orderId);
                    ordOrderService.updateOrderStatus(ordOrder);
                    return Boolean.TRUE;
                }
                return true;
            case "PH":
                if(isEnd) {
                    PhOrdForUpd phOrdForUpd = new PhOrdForUpd();
                    phOrdForUpd.setOrderId(orderId);
                    phOrdForUpd.setStatus(CommonConstant.ORDER_YQS);
                    ordCommonGoodsMapper.updateCommonOrdAPP(phOrdForUpd);
/*                     OrdCommonGoodsForApp ogApp = commonGoodsService.selectDetailByOrderId(orderId, tenantId);
                    Integer[] driverIds;
                    //普货签收时更新司机信息
                   if (ogApp.getOrdCommonTruck().getMdriverId() != null && ogApp.getOrdCommonTruck().getSdriverId() != null) {
                        driverIds = new Integer[2];
                        driverIds[0] = ogApp.getOrdCommonTruck().getMdriverId();
                        driverIds[1] = ogApp.getOrdCommonTruck().getSdriverId();
                    } else {
//                        driverIds = new Integer[1];
//                        driverIds[0] = ogApp.getOrdCommonTruck().getMdriverId();
                        *//*查询司机的车牌号*//*
                        DriverVO driver = trunkFeign.selectDriverStatus(driverId, tenantId);
                        *//*查询目前车辆下的司机信息*//*
                        List<DriverVO> status = trunkFeign.selectDriverStatusByPlateNumber(driver.getPlateNumber());
                        driverIds = new Integer[status.size()];
                        for (int i = 0; i < status.size(); i++) {
                            driverIds[i] = status.get(i).getDriverId();
                        }
                    }
                    DriverVO driverVO = new DriverVO();
                    driverVO.setStatus(CommonConstant.SJKX);
                    driverVO.setDriverIds(driverIds);
                    commonGoodsTruckFeign.updateDriverSta(driverVO);*/
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
}
