package com.zhkj.lc.order.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xiaoleilu.hutool.util.StrUtil;
import com.zhkj.lc.common.api.YunPianSMSUtils;
import com.zhkj.lc.common.constant.CommonConstant;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.R;
import com.zhkj.lc.common.util.UserUtils;
import com.zhkj.lc.common.util.excel.ExcelUtil;
import com.zhkj.lc.common.util.support.Convert;
import com.zhkj.lc.common.vo.*;
import com.zhkj.lc.order.dto.*;
import com.zhkj.lc.order.feign.CommonGoodsTruckFeign;
import com.zhkj.lc.order.feign.OilCardFeign;
import com.zhkj.lc.order.feign.SystemFeginServer;
import com.zhkj.lc.order.feign.TrunkFeign;
import com.zhkj.lc.order.mapper.*;
import com.zhkj.lc.order.model.entity.*;
import com.zhkj.lc.order.service.*;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author cb
 * @since 2019-02-19
 */
@Service
public class NeedPayBillServiceImpl extends ServiceImpl<NeedPayBillMapper, NeedPayBill> implements INeedPayBillService {

    @Autowired
    private NeedPayBillMapper needPayBillMapper;

    @Autowired
    private CommonGoodsTruckFeign truckFeign;
    @Autowired
    private TrunkFeign trunkFeign;

    @Autowired
    private SystemFeginServer systemFeginServer;

    @Autowired
    private OilCardFeign oilCardFeign;

    @Autowired
    private OrdCommonTruckMapper ordCommonTruckMapper;

    @Autowired
    private OrdOrderMapper ordOrderMapper;

    @Autowired
    private IBillMiddleService billMiddleService;

    @Autowired
    private IOrdCommonTruckService ordCommonTruckService;

    @Autowired
    private IOrdOrderService ordOrderService;

    @Autowired
    private IOrdExceptionFeeService exceptionFeeService;

    @Autowired
    private OrderSettlementMapper settlementMapper;

    @Autowired
    private IOrdSettlementService settlementService;

    @Autowired
    private OrdExceptionFeeMapper exceptionFeeMapper;

    @Override
    public Page<NeedPayBaseModel> getCnNeedPayPage(Query query, FinanceQueryDTO financeQueryDTO) {
        List<CnNeedPayDetail> cnPayDetails = needPayBillMapper.selectCnNeedPayList(query, financeQueryDTO);
        //处理账单详情
        System.out.println("13768434343434343434343434343434343434343433223");//435
        System.out.println(cnPayDetails);
        cnPayDetails = caculateCnBill(cnPayDetails, financeQueryDTO);
        System.out.println("1232323243255555555555555555555555555555555555555555555555555555555555");
        System.out.println(cnPayDetails);
        query.setRecords(cnPayDetails);
        return query;
    }

    @Override
    public BigDecimal getTotalFee(FinanceQueryDTO financeQueryDTO) {
        BigDecimal totalFee = new BigDecimal(0);
        switch (financeQueryDTO.getOrderType()){
            case "0" :
                List<CnNeedPayDetail> cnPayDetails = needPayBillMapper.selectCnNeedPayList(financeQueryDTO);
                for(CnNeedPayDetail cn : cnPayDetails){
                    totalFee = totalFee.add(cn.getTransportFee()).add(cn.getExFee());
                }
                break;
            case "1" :
                List<PhNeedPayDetail> phNeedPayDetails = needPayBillMapper.selectPhNeedPayList(financeQueryDTO);
                for(PhNeedPayDetail ph : phNeedPayDetails){
                    totalFee = totalFee.add(ph.getTransportFee()).add(ph.getExFee());
                }
                break;
        }
        return totalFee;
    }

    @Override
    public Page<NeedPayBaseModel> getPhNeedPayPage(Query objectQuery, FinanceQueryDTO financeQueryDTO) {
        List<PhNeedPayDetail> phNeedPayDetails = needPayBillMapper.selectPhNeedPayList(objectQuery, financeQueryDTO);
        //处理账单详情
        phNeedPayDetails = caculatePhBill(phNeedPayDetails, financeQueryDTO);
        objectQuery.setRecords(phNeedPayDetails);
        return objectQuery;
    }

    @Override
    public Page<NeedPayBaseModel> getPdNeedPayPage(Query objectQuery, FinanceQueryDTO financeQueryDTO) {
        List<PdNeedPayDetail> pdNeedPayDetails = needPayBillMapper.selectPdNeedPayList(objectQuery, financeQueryDTO);
        objectQuery.setRecords(pdNeedPayDetails);
        return objectQuery;
    }

    @Override
    public Boolean sendBill(String orderId,String plateNumber, Integer tenantId) {
        try {
            //如果是普货订单
            if (orderId.substring(0, 2).equals("PH")) {
                //更新结算状态为已发送
                OrdCommonTruck ordCommonTruck = new OrdCommonTruck();
                ordCommonTruck.setOrderId(orderId);
                ordCommonTruck.setNeedPayStatus(CommonConstant.YFS);
                ordCommonTruckMapper.updateById(ordCommonTruck);
            } else if (orderId.substring(0, 2).equals("CN")) {
                OrdOrder ordOrder = new OrdOrder();
                ordOrder.setNeedPayStatus(CommonConstant.YFS);
                ordOrder.setOrderId(orderId);
                ordOrder.setUpdateTime(new Date());
                ordOrder.setUpdateBy(UserUtils.getUser());
                OrdOrder condition = new OrdOrder();
                condition.setOrderId(orderId);
                ordOrderMapper.update(ordOrder, new EntityWrapper<>(condition));
            }

            //给司机发送短信
           //DriverVO driverVO = getDriverByDriverId(driverId, tenantId);
            TruTruckOwnVo  truckOwnV = trunkFeign.getTruTruckOwnVoByPlateNumber(plateNumber,tenantId);
            if (truckOwnV != null) {
                YunPianSMSUtils.sendDriverNewBill(truckOwnV.getTruckownPhone(), truckOwnV.getTruckownName(), orderId);
            }
        } catch (Exception e) {
            return Boolean.FALSE;
        }


        return Boolean.TRUE;

    }

    /**
     * 根据司机和账单状态查询相应的账单
     *
     * @param driverId
     * @param billStatus
     * @return
     */
    @Override
    public List<OrderBill> getBillBaseInfo(Integer driverId, String orderId,String billStatus, Integer tenantId) {
        String [] status;
        if(billStatus.equals("3")){
            billStatus = billStatus + ",4";
        }
        if(billStatus.equals("4")){
            billStatus = billStatus + ",3";
        }
        if(billStatus == null || billStatus.replace(" ","").equals("")){
            status = null;
        }else {
            status = billStatus.split(",");
        }
        List<OrderBill> orderBills = needPayBillMapper.getBillBaseInfo(driverId, orderId, status, tenantId);
        for (OrderBill bill : orderBills) {
            bill.setYcFee(new BigDecimal(0));
            List<OrdExceptionFee> exFeeList = exceptionfeeSet(bill.getExceptionFeeList());
            for(OrdExceptionFee fee : exFeeList){
                if(fee.getExceptionFeeType().equals("压车费")){
                    bill.setYcFee(bill.getYcFee().add(fee.getExceptionFee()!=null?fee.getExceptionFee():new BigDecimal(0)));
                }
            }
            bill.setExFee(bill.getExFee().subtract(bill.getYcFee()));
            bill.setExceptionFeeList(exFeeList);
            //获取司机手机号姓名
//            DriverVO driverVO = getDriverByDriverId(driverId, tenantId);
//            TruckVO truckVO = getTruckByPlateNumebr(bill.getPlateNumber(), tenantId);
//            bill.setDriverName(driverVO.getDriverName() != null ? driverVO.getDriverName() : "");
//            bill.setDriverPhone(driverVO.getPhone() != null ? driverVO.getPhone() : "");
            //根据车牌号获取车辆类型
//            String carType = truckVO.getAttribute();
//            cnpd.setCarType(carType != null ? carType : "");
//            bill.setSettlement(bill.getSettlement().equals("0")?"单结":"月结");
            //计算总金额 = 异常费用+运输费
            BigDecimal totalFee = new BigDecimal(0);
            BigDecimal rate = bill.getPayRate() != null ? bill.getPayRate() : new BigDecimal(0);
            totalFee = totalFee.add(bill.getExFee())
                    .add(bill.getTransFee() != null ? bill.getTransFee() : new BigDecimal(0))
                    .add(bill.getYcFee());
            bill.setTransFeeRate(bill.getTransFee());
            bill.setYcFeeRate(bill.getYcFee());
            switch (bill.getIsYfInvoice()){
                // 运输费-0，压车费-0
                case "0" :
                    break;
                // 运输费-0，压车费-1
                case "1" :
                    bill.setYcFeeRate(bill.getYcFee().multiply(
                            new BigDecimal(1).subtract(
                                    rate.multiply(new BigDecimal(0.01)))
                    ).setScale(2, BigDecimal.ROUND_HALF_UP));
                    break;
                // 运输费-1，压车费-0
                case "2" :
                    bill.setTransFeeRate(bill.getTransFee().multiply(
                            new BigDecimal(1).subtract(
                                    rate.multiply(new BigDecimal(0.01)))
                    ).setScale(2, BigDecimal.ROUND_HALF_UP));
                    break;
                // 运输费-1，压车费-1
                case "3" :
                    bill.setYcFeeRate(bill.getYcFee().multiply(
                            new BigDecimal(1).subtract(
                                    rate.multiply(new BigDecimal(0.01)))
                    ).setScale(2, BigDecimal.ROUND_HALF_UP));
                    bill.setTransFeeRate(bill.getTransFee().multiply(
                            new BigDecimal(1).subtract(
                                    rate.multiply(new BigDecimal(0.01)))
                    ).setScale(2, BigDecimal.ROUND_HALF_UP));
                    break;
            }
            //含利率的总费用
            bill.setTotalFee(bill.getTransFeeRate().add(bill.getYcFeeRate().add(bill.getExFee())));

            //应付现金 = 分配现金-正常油卡费用

            /*正常油卡费*/
            bill.setCommonOilFee(bill.getTotalFee().subtract(bill.getPayCash()).subtract(bill.getTransOilFee()));

            /*地址处理*/
            bill.setStartPlace(bill.getPickupAdds().get(0).getAddressCity());
            bill.setEndPlace(bill.getArrivalAdds().get(bill.getArrivalAdds().size()-1).getAddressCity());
            if(bill.getType()!=null && bill.getType().equals("0")){
                bill.setStartPlace(bill.getPickCnPlace());
            }else if(bill.getType()!=null && bill.getType().equals("1")){
                bill.setEndPlace(bill.getReturnCnPlace());
            }
        }
        return orderBills;
    }

    /**
     * 根据司机和对账单状态查询相应的对账单信息
     *
     * @param driverId
     * @param billStatus
     * @return
     */
    @Override
    public List<NeedPayBill> accountBillBaseInfo(Integer driverId, String billStatus, Integer tenantId) {
        /*获取对账单列表*/
        FinanceQueryDTO dto = new FinanceQueryDTO();
        dto.setDriverId(driverId);
        if(billStatus.equals("3")){
            String[] statuss = {"4","3"};
            dto.setStatusArray(statuss);
        }else{
            dto.setSettlementStatus(billStatus);
        }
        List<NeedPayBill> needPayBills = needPayBillMapper.needPayBillList(dto);
        for(NeedPayBill bill : needPayBills){
            List<OrderBill> orderBills = new ArrayList<>();
            /*获取 中间表 对应订单*/
            BillMiddle param = new BillMiddle();
            param.setAccountPayNumber(bill.getAccountPayId());
            param.setTenantId(bill.getTenantId());
            List<BillMiddle> billMiddles = billMiddleService.selectByAccountPayNumber(param);
            /*获取订单信息*/
            String orderIds[] = new String[billMiddles.size()];
            for(int i=0; i<billMiddles.size(); i++){
                orderIds[i] = billMiddles.get(i).getOrderNumber();
            }
            if(orderIds.length==0){
                return null;
            }
            /*根据订单编号查询*/
            switch (bill.getOrderType()){
                case "1":
                    bill.setOrderType("普货");
                    List<PhNeedPayDetail> ppdList = selectPhBillToDZD(orderIds, tenantId);
                    for(PhNeedPayDetail pd : ppdList){
                        OrderBill orderBill = new OrderBill();
                        orderBill.setOrderId(pd.getOrderId());
                        orderBill.setOrderDate(pd.getStartTime());
                        //处理发货地
                        orderBill.setStartPlace(OrdExceptionConditionServiceImpl.getPlace(pd.getPickGoodsPlace()));
                        //处理到货地
                        orderBill.setEndPlace(OrdExceptionConditionServiceImpl.getPlace(pd.getSendGoodsPlace()));
                        // 获取异常费用信息
                        BigDecimal excepFee = new BigDecimal("0");
                        for(OrdExceptionFee fee : pd.getExceptionFeeList()){
                            excepFee = excepFee.add(fee.getExceptionFee());
                        }
                        /*异常费用*/
                        orderBill.setExFee(excepFee);
                        /*运输费*/
                        orderBill.setTransFee(pd.getTransportFee());
                        pd.setTransportFee(pd.getTransportFee()!=null?pd.getTransportFee():new BigDecimal(0));
                        BigDecimal totalFee = new BigDecimal("0");
                        totalFee = pd.getTransportFee().add(excepFee!=null?excepFee:new BigDecimal("0"));
                        orderBill.setTotalFee(totalFee);
                        orderBills.add(orderBill);
                    }
//                    bill.setPpdList(ppdList);
                    break;
                case "0":
                    bill.setOrderType("集装箱");
                    List<CnNeedPayDetail> cnpdList = selectCnBillToDZD(orderIds, tenantId);
                    for(CnNeedPayDetail pd : cnpdList){
                        OrderBill orderBill = new OrderBill();
                        orderBill.setOrderId(pd.getOrderId());
                        orderBill.setOrderDate(pd.getType().equals("1")?pd.getStartTimeh():pd.getStartTimeq());
                        //处理发货地
                        orderBill.setStartPlace(OrdExceptionConditionServiceImpl.getPlace(pd.getPickGoodsPlace()));
                        //处理到货地
                        orderBill.setEndPlace(OrdExceptionConditionServiceImpl.getPlace(pd.getSendGoodsPlace()));
                        if(pd.getType()!=null && pd.getType().equals("0")){
                            orderBill.setStartPlace(pd.getPickCNPlace());
                        }else if(pd.getType()!=null && pd.getType().equals("1")){
                            orderBill.setEndPlace(pd.getReturnCNPlace());
                        }
                        // 获取异常费用信息
                        BigDecimal excepFee = new BigDecimal("0");
                        for(OrdExceptionFee fee : pd.getExceptionFeeList()){
                            excepFee = excepFee.add(fee.getExceptionFee());
                        }
                        /*异常费用*/
                        orderBill.setExFee(excepFee);
                        orderBill.setTransFee(pd.getTransportFee());
                        pd.setTransportFee(pd.getTransportFee()!=null?pd.getTransportFee():new BigDecimal(0));
                        BigDecimal totalFee = new BigDecimal("0");
                        totalFee = pd.getTransportFee().add(excepFee!=null?excepFee:new BigDecimal("0"));
                        orderBill.setTotalFee(totalFee);
                        orderBills.add(orderBill);
                    }
//                    bill.setCnpdList(cnpdList);
                    break;
            }
            bill.setOrderBills(orderBills);
        }
        return needPayBills;
    }

    /**
     * 司机确认账单
     *
     * @param orderId
     * @return
     */
    @Override
    public Boolean confirmBill(String orderId) {
        try {
            //如果是普货
            if (orderId.substring(0, 2).equals("PH")) {
                OrdCommonTruck ordCommonTruck = new OrdCommonTruck();
                ordCommonTruck.setOrderId(orderId);
                //司机已确认
                ordCommonTruck.setNeedPayStatus(CommonConstant.YQR);
                ordCommonTruckMapper.updateById(ordCommonTruck);
            }
            //如果是集装箱
            else {
                OrdOrder ordOrder = new OrdOrder();
                ordOrder.setOrderId(orderId);
                ordOrder.setNeedPayStatus(CommonConstant.YQR);
                ordOrder.setUpdateTime(new Date());
                OrdOrder conditon = new OrdOrder();
                conditon.setOrderId(orderId);
                ordOrderMapper.update(ordOrder, new EntityWrapper<>(conditon));
            }
        } catch (Exception e) {
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    @Override
    @Transactional
    public Boolean confirm(Integer id, String accountPayId, String flag, String repaynumber, Integer tenantId, String loginUser) {
        NeedPayBill needPayBill = new NeedPayBill();
        needPayBill.setId(id);
        needPayBill.setRepaynumber(repaynumber);
        needPayBill.setUpdateTime(new Date());
        needPayBill.setSettlementStatus(CommonConstant.YJS);
        needPayBillMapper.updateById(needPayBill);

        /*保存结算数据*/
        OrderSettlement settlement = settlementMapper.selectSettlementByOrderId(accountPayId);
        settlement.setCashBy(loginUser);
        settlement.setCashTime(new Date());
        settlement.setCashStatus(2);
        settlement.setOilBy(loginUser);
        settlement.setOilStatus(2);
        settlement.setOilTime(new Date());
        settlementMapper.updateById(settlement);
        /*将分配的运输油卡充值到车辆运输油卡余额中*/
        NeedPayBill bill = needPayBillMapper.selectById(id);
        TruckVO truck = trunkFeign.selectTruckByPlateNumber(bill.getPlateNumber(), bill.getTenantId());
        OilTruckRechargeVO rechargesVO = new OilTruckRechargeVO();
        rechargesVO.setTenantId(tenantId);
        rechargesVO.setCreateBy(loginUser);
        rechargesVO.setTruckId(truck.getTruckId());
        rechargesVO.setPlateNumber(bill.getPlateNumber());
        rechargesVO.setRecharge(bill.getFreightOilcardFee());
        oilCardFeign.add(rechargesVO);
        /*将对账单下的订单信息更新为对账单的状态*/
        List<BillMiddle> billMiddles = billMiddleService.selectOrderIdByaccId(id, tenantId);
        for(BillMiddle b : billMiddles){
            switch (b.getOrderNumber().substring(0,2)){
                case "PH":
                    OrdCommonTruck commonTruck = new OrdCommonTruck();
                    commonTruck.setOrderId(b.getOrderNumber());
                    commonTruck.setNeedPayStatus(CommonConstant.YJS);
                    commonTruck.setIsYFInvoice(flag);
                    OrdCommonTruck co_param = new OrdCommonTruck();
                    co_param.setOrderId(b.getOrderNumber());
                    ordCommonTruckService.update(commonTruck, new EntityWrapper<>(co_param));
                    break;
                case "CN":
                    OrdOrder ordOrder = new OrdOrder();
                    ordOrder.setOrderId(b.getOrderNumber());
                    ordOrder.setNeedPayStatus(CommonConstant.YJS);
                    ordOrder.setIsYFInvoice(flag);
                    OrdOrder param = new OrdOrder();
                    param.setOrderId(b.getOrderNumber());
                    ordOrderService.update(ordOrder, new EntityWrapper<>(param));
                    break;
            }
        }
        return true;
    }

    /**
     * 小程序司机反馈账单
     *
     * @param orderId
     * @param feedMsg
     * @return
     */
    @Override
    public Boolean feedback(String orderId, String feedMsg) {
        try {
            if (orderId.substring(0, 2).equals("PH")) {
                OrdCommonTruck ordCommonTruck = new OrdCommonTruck();
                //司机已反馈
                ordCommonTruck.setNeedPayStatus(CommonConstant.YFK);
                ordCommonTruck.setOrderId(orderId);
                ordCommonTruck.setFeedBack(feedMsg);
                ordCommonTruckMapper.updateById(ordCommonTruck);
            } else {
                OrdOrder ordOrder = new OrdOrder();
                ordOrder.setOrderId(orderId);
                ordOrder.setUpdateTime(new Date());
                ordOrder.setNeedPayStatus(CommonConstant.YFK);
                ordOrder.setFeedBack(feedMsg);
                OrdOrder condition = new OrdOrder();
                condition.setOrderId(orderId);
                ordOrderMapper.update(ordOrder, new EntityWrapper<>(condition));
            }
        } catch (Exception e) {
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    /**
     * 司机撤销并确认账单
     *
     * @param orderId
     * @return
     */
    @Override
    public Boolean cancleFeedback(String orderId) {
        try {
            if (orderId.substring(0, 2).equals("PH")) {
                OrdCommonTruck ordCommonTruck = new OrdCommonTruck();
                ordCommonTruck.setOrderId(orderId);
                ordCommonTruck.setFeedBack("");
                ordCommonTruck.setNeedPayStatus(CommonConstant.YFS);
                ordCommonTruckMapper.updateById(ordCommonTruck);
            } else {
                OrdOrder ordOrder = new OrdOrder();
                OrdOrder condition = new OrdOrder();
                ordOrder.setOrderId(orderId);
                ordOrder.setFeedBack("");
                ordOrder.setNeedPayStatus(CommonConstant.YFS);
                ordOrder.setUpdateTime(new Date());
                condition.setOrderId(orderId);
                ordOrderMapper.update(ordOrder, new EntityWrapper<>(condition));
            }
        } catch (Exception e) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * 分配现金
     *
     * @param orderId
     * @param payCash
     * @return
     */
    @Override
    public Boolean designCash(String orderId, String ifInvoice, BigDecimal payCash, BigDecimal cash, BigDecimal etcFee, BigDecimal oilPledge,BigDecimal transOilFee,BigDecimal rate) {
        //更新现金金额
        try {
            if (orderId.substring(0, 2).equals("PH")) {
                OrdCommonTruck commonTruck = new OrdCommonTruck();
                commonTruck.setOrderId(orderId);
                commonTruck.setPayCash(payCash);
                commonTruck.setCash(cash);
                commonTruck.setTransOilFee(transOilFee);
                commonTruck.setOilPledge(oilPledge);
                commonTruck.setEtcFee(etcFee);
                commonTruck.setIsYFInvoice(ifInvoice);
                commonTruck.setNeedPayStatus(CommonConstant.WFS);
                commonTruck.setPayRate(rate);
                OrdCommonTruck common = new OrdCommonTruck();
                common.setOrderId(orderId);
                ordCommonTruckMapper.update(commonTruck,new EntityWrapper<>(common));
            } else {
                OrdOrder ordOrder = new OrdOrder();
                ordOrder.setOrderId(orderId);
                ordOrder.setNeedPayStatus(CommonConstant.WFS);
                ordOrder.setPayCash(payCash);
                ordOrder.setCash(cash);
                ordOrder.setTransOilFee(transOilFee);
                ordOrder.setOilPledge(oilPledge);
                ordOrder.setEtcFee(etcFee);
                ordOrder.setIsYFInvoice(ifInvoice);
                ordOrder.setUpdateTime(new Date());
                ordOrder.setUpdateBy(UserUtils.getUser());
                ordOrder.setPayRate(rate);
                OrdOrder condition = new OrdOrder();
                condition.setOrderId(orderId);
                ordOrderMapper.update(ordOrder, new EntityWrapper<>(condition));
            }
        } catch (Exception e) {
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    /**
     * pc端确认账单
     *
     * @param orderId
     * @param repaynumber 报销单号
     * @return
     */
    @Transactional
    @Override
    public Boolean pcConfirmBill(String orderId, String repaynumber, Integer tenantId, String loginUser ) {
        if (orderId.substring(0, 2).equals("PH")) {
            OrdCommonTruck ordCommonTruck = new OrdCommonTruck();
            ordCommonTruck.setNeedPayStatus(CommonConstant.YJS);
            ordCommonTruck.setOrderId(orderId);
            ordCommonTruck.setRepaynumber(repaynumber);
            ordCommonTruckMapper.updateById(ordCommonTruck);
        } else {
            OrdOrder ordOrder = new OrdOrder();
            ordOrder.setOrderId(orderId);
            ordOrder.setNeedPayStatus(CommonConstant.YJS);
            ordOrder.setUpdateBy(loginUser);
            ordOrder.setUpdateTime(new Date());
            ordOrder.setRepaynumber(repaynumber);
            OrdOrder condition = new OrdOrder();
            condition.setOrderId(orderId);
            ordOrderMapper.update(ordOrder, new EntityWrapper<>(condition));
        }
        /*保存结算数据*/
        OrderSettlement settlement = settlementMapper.selectSettlementByOrderId(orderId);
        if (settlement != null) {
            settlement.setCashBy(loginUser);
            settlement.setCashStatus(2);
            settlement.setCashTime(new Date());
            settlementMapper.updateById(settlement);
        }else {
            OrderSettlement orderSettlement = new OrderSettlement();
            orderSettlement.setOrderId(orderId);
            orderSettlement.setCashBy(loginUser);
            orderSettlement.setCashStatus(2);
            orderSettlement.setCashTime(new Date());
            settlementMapper.insert(orderSettlement);
        }
        /*改成车辆*/
        // # TODO SD
//        NeedPayBill needPayBill = needPayBillMapper.selectFreightOilcardFeeByOrderId(orderId,null, tenantId);
//        if (null != needPayBill && "5".equals(needPayBill.getSettlementStatus())) {
//            DriverVO driverVOResult = trunkFeign.getDriverByid(needPayBill.getDriverId());
//            DriverVO driverVO = new DriverVO();
//            driverVO.setDriverId(needPayBill.getDriverId());
//            driverVO.setFreightOilcardAmount(driverVOResult.getFreightOilcardAmount().add(needPayBill.getFreightOilcardFee()));
//            driverVO.setPhone(driverVOResult.getPhone());
//            trunkFeign.edit(driverVO);
//        }
        return Boolean.TRUE;
    }

    /**
     * 根据订单编号查询账单详情
     *
     * @param orderId
     * @return
     */
    @Override
    public NeedPayBaseModel getBillDetailInfoForPc(String orderId,Integer tenantId) {
        FinanceQueryDTO financeQueryDTO = new FinanceQueryDTO();
        financeQueryDTO.setOrderId(orderId);
        financeQueryDTO.setTenantId(tenantId);
        boolean isph = orderId.substring(0, 2).equals("PH");
        if (isph) {
            List<PhNeedPayDetail> phNeedPayDetails = needPayBillMapper.selectPhNeedPayList(financeQueryDTO);
            if(phNeedPayDetails!=null && phNeedPayDetails.size() == 1) {
                phNeedPayDetails = caculatePhBill(phNeedPayDetails, financeQueryDTO);
                return phNeedPayDetails.get(0);
            }
        } else {
            List<CnNeedPayDetail> cnPayDetails = needPayBillMapper.selectCnNeedPayList(financeQueryDTO);
            if(cnPayDetails != null && cnPayDetails.size() == 1) {
                cnPayDetails = caculateCnBill(cnPayDetails, financeQueryDTO);
                return cnPayDetails.get(0);
            }
        }
        return null;
    }

    /**
     * 根据查询条件导出
     *
     * @param request
     * @param response
     * @param queryDTO
     * @return
     */
    @Override
    public Boolean exportBillByQuery(HttpServletRequest request, HttpServletResponse response, FinanceQueryDTO queryDTO) {
        //导出集装箱账单
        try {
            if (queryDTO.getOrderType().equals("0")) {
                List<CnNeedPayDetail> cnPayDetails = needPayBillMapper.selectCnNeedPayList(new Query(new HashMap<>()), queryDTO);
                List<CnNeedPayDetail> exportList = caculateCnBill(cnPayDetails, queryDTO);
                for (CnNeedPayDetail cnpd : exportList) {
                    //处理导出数据

                    //结算方式
                    if (cnpd.getSettlement() != null) {
                        cnpd.setSettlement(cnpd.getSettlement().equals("0") ? "单结" : "月结");
                    }
                    if (cnpd.getCarType() != null) {
                        //车辆类型
                        switch (cnpd.getCarType()) {
                            case "0":
                                cnpd.setCarType("集卡车");
                                break;
                            case "1":
                                cnpd.setCarType("半吊车");
                                break;
                            case "2":
                                cnpd.setCarType("高栏车");
                                break;
                            default:
                                break;
                        }
                    }
                    if (cnpd.getNeedPayStatus() != null) {
                        cnpd.setNeedPayStatus(getBillStatus(cnpd.getNeedPayStatus()));
                    }
                }
                DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                String excelName = fmt.format(new Date()) + "-集装箱账单信息";
                ExcelUtil<CnNeedPayDetail> util = new ExcelUtil<>(CnNeedPayDetail.class);
                util.exportExcel(request, response, cnPayDetails, excelName, null);
            }
            //导出普货账单
            else {
                List<PhNeedPayDetail> phNeedPayDetails = needPayBillMapper.selectPhNeedPayList(new Query(new HashMap<>()), queryDTO);
                List<PhNeedPayDetail> exportList = caculatePhBill(phNeedPayDetails, queryDTO);
                //处理导出数据
                for (PhNeedPayDetail ppd : exportList) {
                    //车辆类型
                    //结算方式
                    //结算状态
                    if (ppd.getSettlement() != null) {
                        ppd.setSettlement(ppd.getSettlement().equals("0") ? "单结" : "月结");
                    }
                    if (ppd.getCarType() != null) {
                        switch (ppd.getCarType()) {
                            case "0":
                                ppd.setCarType("集卡车");
                                break;
                            case "1":
                                ppd.setCarType("半吊车");
                                break;
                            case "2":
                                ppd.setCarType("高栏车");
                                break;
                            default:
                                break;
                        }
                    }
                    if (ppd.getNeedPayStatus() != null) {
                        ppd.setNeedPayStatus(getBillStatus(ppd.getNeedPayStatus()));
                    }
                }
                DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                String excelName = fmt.format(new Date()) + "-普货账单信息";
                ExcelUtil<PhNeedPayDetail> util = new ExcelUtil<>(PhNeedPayDetail.class);
                util.exportExcel(request, response, exportList, excelName, null);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 加入对账单
     *
     * @param queryDTO
     * @return
     */
    @Transactional
    @Override
    public Boolean addToDZD(FinanceQueryDTO queryDTO) {
        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            /*零费用*/
            BigDecimal zero = new BigDecimal(0);

            if (queryDTO.getOrderIds() != null && queryDTO.getOrderIds().length > 0) {
                List<BillMiddle> billMiddles = new ArrayList<>();
                //生成对账单编号
                String dzdNumebr = getNeedPayableNumber(queryDTO.getTenantId());
                for (int i = 0; i < queryDTO.getOrderIds().length; i++) {
                    BillMiddle billMiddle = new BillMiddle();
                    billMiddle.setAccountPayNumber(dzdNumebr);
                    billMiddle.setOrderNumber(queryDTO.getOrderIds()[i]);
                    billMiddle.setTenantId(queryDTO.getTenantId());
                    billMiddles.add(billMiddle);
                }
                //批量插入中间表
                billMiddleService.insertBatch(billMiddles);
                //处理对账单下的订单的数据
                NeedPayBill needPayBill = new NeedPayBill();
                needPayBill.setPlateNumber(queryDTO.getPlateNumber());
                needPayBill.setAccountPayId(dzdNumebr);
                /*对账单状态*/
                needPayBill.setSettlementStatus(CommonConstant.WFP);
                //订单数
                needPayBill.setOrderAmount(billMiddles.size());

                needPayBill.setDateEnd(format.parse(queryDTO.getBlEndTime()));
                needPayBill.setDateStart(format.parse(queryDTO.getBlStartTime()));
                //日期
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                //根据订单编号获得订单的账单集合
                if (queryDTO.getOrderIds()[0].substring(0, 2).equals("PH")) {
                    /*普货订单接口*/
                    List<PhNeedPayDetail> ppdList = needPayBillMapper.selectPhBillToDZD(queryDTO.getOrderIds(), queryDTO.getTenantId());
                    ppdList = caculatePhBill(ppdList, queryDTO);
                    //不带利率的总费用
                    BigDecimal totalFee = new BigDecimal(0);
                    //带利率的总费用
                    BigDecimal totalFeeByRate = new BigDecimal(0);
                    //正常油卡费总计
                    BigDecimal totalOilFee = new BigDecimal(0);
                    //ETC费用
                    BigDecimal totalETCFee = new BigDecimal(0);

                    //应付金额总计

                    //运费油卡总计

                    for (PhNeedPayDetail ppd : ppdList) {
                        totalFee = totalFee.add(ppd.getNeedPay()!=null?ppd.getNeedPay():zero);/*应付总额*/
                        totalFeeByRate = totalFeeByRate.add(ppd.getNeedPayByRate()!=null?ppd.getNeedPayByRate():zero);/*应付总额(含税)*/
                        totalOilFee = totalOilFee.add(ppd.getCommonOilFee()!=null?ppd.getCommonOilFee():zero);
                        totalETCFee = totalETCFee.add(ppd.getEtcFee()!=null?ppd.getEtcFee():zero);
                    }
                    needPayBill.setNormalOilcardFee(totalOilFee);
                    needPayBill.setEtcFee(totalETCFee);
                    //利率
                    needPayBill.setRate(ppdList.get(0).getMoneyRate());
                    needPayBill.setTotalFee(totalFee);
                    needPayBill.setTotalFeeRate(totalFeeByRate);
                    needPayBill.setOrderType("1");
                    //新增对账单预处理
                    needPayBill = preInsert(needPayBill, queryDTO);

                    //普货订单已加入对账单
                    List<OrdCommonTruck> updateList = new ArrayList<>();
                    for (int i = 0;i<queryDTO.getOrderIds().length;i++){
                        /*订单司机信息*/
                        OrdCommonTruck ordCommonTruck = new OrdCommonTruck();
                        ordCommonTruck.setOrderId(queryDTO.getOrderIds()[i]);
                        ordCommonTruck.setIfAddToYFBill("1");
                        updateList.add(ordCommonTruck);
                    }
                    ordCommonTruckService.updateBatchById(updateList);
                } else {
                    List<CnNeedPayDetail> cnpdList = needPayBillMapper.selectCnBillToDZD(queryDTO.getOrderIds(),queryDTO.getTenantId());
                    cnpdList = caculateCnBill(cnpdList, queryDTO);
                    BigDecimal totalFee = new BigDecimal(0);
                    BigDecimal totalFeeByRate = new BigDecimal(0);
                    BigDecimal totalOilFee = new BigDecimal(0);
                    BigDecimal totalETCFee = new BigDecimal(0);
                    for (CnNeedPayDetail cnpd : cnpdList) {
                        totalFee = totalFee.add(cnpd.getNeedPay()!=null?cnpd.getNeedPay():zero);
                        totalFeeByRate = totalFeeByRate.add(cnpd.getNeedPayByRate()!=null?cnpd.getNeedPayByRate():zero);
                        totalOilFee = totalOilFee.add(cnpd.getCommonOilFee()!=null?cnpd.getCommonOilFee():zero);
                        totalETCFee = totalETCFee.add(cnpd.getEtcFee()!=null?cnpd.getEtcFee():zero);
                    }
                    needPayBill.setEtcFee(totalETCFee);
                    needPayBill.setNormalOilcardFee(totalOilFee);
                    needPayBill.setRate(cnpdList.get(0).getMoneyRate());
                    needPayBill.setTotalFee(totalFee);
                    needPayBill.setTotalFeeRate(totalFeeByRate);
                    needPayBill.setOrderType("0");
                    needPayBill = preInsert(needPayBill, queryDTO);

                    //集装箱加入对账单
                    for (int i = 0;i<queryDTO.getOrderIds().length;i++){
                        OrdOrder ordOrder = new OrdOrder();
                        ordOrder.setOrderId(queryDTO.getOrderIds()[i]);
                        ordOrder.setIfAddToYfbill("1");
                        ordOrder.setUpdateTime(new Date());
                        OrdOrder condition = new OrdOrder();
                        condition.setOrderId(queryDTO.getOrderIds()[i]);
                        ordOrderMapper.update(ordOrder,new EntityWrapper<>(condition));
                    }
                }
                needPayBillMapper.insert(needPayBill);
                OrderSettlement settlement = new OrderSettlement();
                settlement.setOrderId(needPayBill.getAccountPayId());
                settlementMapper.insert(settlement);
            }

        } catch (Exception e) {
            e.printStackTrace();
            //手动回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * 根据车牌号获取车辆信息
     *
     * @param plateNumebr
     * @param tenantId
     * @return
     */
    public TruckVO getTruckByPlateNumebr(String plateNumebr, Integer tenantId) {
        TruckVO truckVO = new TruckVO();
        truckVO.setPlateNumber(plateNumebr);
        truckVO.setTenantId(tenantId);
        TruckVO result = truckFeign.getZYTruckList(truckVO).get(0);
        return result;
    }

    /**
     * 根据司机id获取司机信息
     *
     * @param driverId
     * @param tenantId
     * @return
     */
    public DriverVO getDriverByDriverId(Integer driverId, Integer tenantId) {
        DriverVO condition = new DriverVO();
        condition.setDriverId(driverId);
        condition.setTenantId(tenantId);
        List<DriverVO> list = truckFeign.getDriverListByPlateId(condition);
        DriverVO result = new DriverVO();
        if(null != list && list.size() > 0){
            result = list.get(0);
        }
        return result;
    }

    /**
     * 将利率转为百分比字符串
     *
     * @param rate
     * @return
     */
    public String getRateStr(BigDecimal rate) {
        NumberFormat percent = NumberFormat.getPercentInstance();
        percent.setMaximumFractionDigits(2);
        return percent.format(rate.doubleValue()/100);
    }

    /**
     * 处理集装箱应付账单数据
     *
     * @param list
     * @param queryDTO
     * @return
     */
    public List<CnNeedPayDetail> caculateCnBill(List<CnNeedPayDetail> list, FinanceQueryDTO queryDTO) {
        List<CnNeedPayDetail> result = new ArrayList<>();
        Integer tenantId = queryDTO.getTenantId();
        System.out.println("____________________________________________________");
        System.out.println(list);
        for (CnNeedPayDetail cnpd : list) {

              /*车主信息处理*/
            TruTruckOwnVo truTruckOwnVo = trunkFeign.getTruTruckOwnVoByPlateNumber(cnpd.getPlateNumber(), tenantId);
            cnpd.setTruckownName(truTruckOwnVo.getTruckownName());

            /********************异常费用列表*******************/
            List<OrdExceptionFee> exFeeList = exceptionfeeSet(cnpd.getExceptionFeeList());
            for(OrdExceptionFee fee : exFeeList){
                if(fee.getExceptionFeeType().equals("压车费")){
                    cnpd.setYcFee(cnpd.getYcFee().add(fee.getExceptionFee()!=null?fee.getExceptionFee():new BigDecimal(0)));
                }
            }
            cnpd.setExFee(cnpd.getExFee().subtract(cnpd.getYcFee()));
            cnpd.setExceptionFeeList(exFeeList);
            //获取司机手机号姓名
            DriverVO driverVO = getDriverByDriverId(cnpd.getDriverId(), queryDTO.getTenantId());
            TruckVO truckVO = getTruckByPlateNumebr(cnpd.getPlateNumber(), queryDTO.getTenantId());
            cnpd.setDriverName(driverVO.getDriverName() != null ? driverVO.getDriverName() : "");
            cnpd.setDriverPhone(driverVO.getPhone() != null ? driverVO.getPhone() : "");
            //根据车牌号获取车辆类型
            String carType = truckVO.getAttribute();
            cnpd.setCarType(carType != null ? carType : "");
            /*获取结算方式
            if(("2").equals(carType)){
                cnpd.setSettlement("单结");
            }else if(driverVO.getPayWay() == null){
                cnpd.setSettlement("月结");
            }else {
                cnpd.setSettlement(driverVO.getPayWay());
            }
            如果输入月结、单结条件
            if(queryDTO.getSettlement() != null){
                if(!queryDTO.getSettlement().equals(cnpd.getSettlement())){
                    continue;
                }
            }*/
            cnpd.setSettlement(cnpd.getSettlement().equals("0")?"单结":"月结");
            //计算总金额 = 异常费用+运输费
            BigDecimal totalFee = new BigDecimal(0);
            BigDecimal rate = cnpd.getMoneyRate() != null ? cnpd.getMoneyRate() : new BigDecimal(0);
            totalFee = totalFee.add(cnpd.getExFee())
                        .add(cnpd.getTransportFee() != null ? cnpd.getTransportFee() : new BigDecimal(0))
                        .add(cnpd.getYcFee());
            cnpd.setTransportFeeRate(cnpd.getTransportFee());
            cnpd.setYcFeeRate(cnpd.getYcFee());
            switch (cnpd.getIsYFInvoice()){
                // 运输费-0，压车费-0
                case "0" :
                    break;
                // 运输费-0，压车费-1
                case "1" :
                    cnpd.setYcFeeRate(cnpd.getYcFee().multiply(
                            new BigDecimal(1).subtract(
                                    rate.multiply(new BigDecimal(0.01)))
                    ).setScale(2, BigDecimal.ROUND_HALF_UP));
                    break;
                // 运输费-1，压车费-0
                case "2" :
                    cnpd.setTransportFeeRate(cnpd.getTransportFee().multiply(
                            new BigDecimal(1).subtract(
                                    rate.multiply(new BigDecimal(0.01)))
                    ).setScale(2, BigDecimal.ROUND_HALF_UP));
                    break;
                // 运输费-1，压车费-1
                case "3" :
                    cnpd.setYcFeeRate(cnpd.getYcFee().multiply(
                            new BigDecimal(1).subtract(
                                    rate.multiply(new BigDecimal(0.01)))
                    ).setScale(2, BigDecimal.ROUND_HALF_UP));
                    cnpd.setTransportFeeRate(cnpd.getTransportFee().multiply(
                            new BigDecimal(1).subtract(
                                    rate.multiply(new BigDecimal(0.01)))
                    ).setScale(2, BigDecimal.ROUND_HALF_UP));
                    break;
            }
            //含利率的总费用
            cnpd.setNeedPayByRate(cnpd.getTransportFeeRate().add(cnpd.getYcFeeRate().add(cnpd.getExFee())));

            /*利率*/
            cnpd.setRateStr(getRateStr(rate));
            //应付现金 = 分配现金-正常油卡费用
            /****************正常油卡费统计*******************/
            RechangeNormalVO vo = new RechangeNormalVO();
            vo.setTenantId(queryDTO.getTenantId());
            vo.setIsPassed("1");
            vo.setTruckId(driverVO.getPlateId());
            /*判断结算方式*/
            if (cnpd.getSettlement().equals("单结")) {
                vo.setOrderId(cnpd.getOrderId());
            } else {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                if(cnpd.getType().equals("0")){//去程
                    vo.setBeginTime(format.format(cnpd.getStartTimeq()));
                    vo.setEndTime(format.format(cnpd.getEndTimeq()));
                }else {
                    vo.setBeginTime(format.format(cnpd.getStartTimeh()));
                    vo.setEndTime(format.format(cnpd.getEndTimeh()));
                }
            }
            List<RechangeNormalVO> oilList = oilCardFeign.getRechangeNormalList(vo);
            BigDecimal rechargeSum = new BigDecimal("0");
            for (RechangeNormalVO oil : oilList) {
                rechargeSum = rechargeSum.add(oil.getRechargeSum());
            }
            /*正常油卡费*/
            cnpd.setCommonOilFee(rechargeSum);

            cnpd.setNeedPay(totalFee);
            if(!cnpd.getNeedPayStatus().equals("0")){ /*如果已分配*/
                if(cnpd.getIsYFInvoice().equals("1")){
                    cnpd.setNeedPay(cnpd.getNeedPayByRate());
                }
            }
            /*地址处理*/
            String ways = "";
            String tiAdress="";
            String songAdress="";
            for(int i = 0; i < cnpd.getPickupAdds().size(); i++){
                if(i != 0){
                    ways += cnpd.getPickupAdds().get(i).getAddressCity()+",";
                }
                tiAdress+="-"+cnpd.getPickupAdds().get(i).getAddressCity();
            }
            cnpd.setPickGoodsPlace(tiAdress.replaceFirst("-",""));
            for(int i = 0; i < cnpd.getArrivalAdds().size(); i++){
                if(i != cnpd.getArrivalAdds().size()-1){
                    ways += cnpd.getArrivalAdds().get(i).getAddressCity()+",";
                }
                songAdress+="-"+cnpd.getArrivalAdds().get(i).getAddressCity();
            }
            cnpd.setSendGoodsPlace(songAdress.replaceFirst("-",""));
            cnpd.setWaypoints(ways);
            result.add(cnpd);
        }
        return result;
    }

    /**
     * 分页查询应付对账单列表
     *
     * @param query           分页数据
     * @param financeQueryDTO 查询条件
     * @return
     */
    @Override
    public Page<NeedPayBill> needPayBillList(Query query, FinanceQueryDTO financeQueryDTO) {
        Integer tenantId=financeQueryDTO.getTenantId();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if(null!=financeQueryDTO.getTruckOwnId()&&financeQueryDTO.getTruckOwnId()!="") {
            financeQueryDTO.setPlates(getPlateNumbers(Integer.valueOf(financeQueryDTO.getTruckOwnId()),tenantId));
        }
        List<NeedPayBill> needPayBills = needPayBillMapper.needPayBillList(query, financeQueryDTO);
        if(needPayBills.size()!=0) {
            for (NeedPayBill npb : needPayBills) {
                /*车主信息处理*/
                TruTruckOwnVo truTruckOwnVo = trunkFeign.getTruTruckOwnVoByPlateNumber(npb.getPlateNumber(), tenantId);
                npb.setTruckownName(truTruckOwnVo.getTruckownName());
                npb.setTruckownPhone(truTruckOwnVo.getTruckownPhone());
                Integer truckId = getTruckId(truTruckOwnVo.getTruckList(), npb.getPlateNumber());
                OilTruckRechargeVO rechargesVO = oilCardFeign.findBalanceByTruckId(truckId, financeQueryDTO.getTenantId());
                npb.setOilTransFee(new BigDecimal(0));
                if(rechargesVO != null){
                    npb.setOilTransFee(rechargesVO.getBalance()!=null?rechargesVO.getBalance():new BigDecimal(0));
                }
                /*日期范围处理*/
                String start = format.format(npb.getDateStart());
                String end = format.format(npb.getDateEnd());
                npb.setDateLimit(start + "~" + end);
                /*司机信息处理*/
                DriverVO driverVO = getDriverByDriverId(npb.getDriverId(), tenantId);
                npb.setDriverName(driverVO != null ? driverVO.getDriverName() : "");

                npb.setYcFee(exceptionFeeMapper.selectYcFeeByAccountId(npb.getAccountPayId()));
                npb.setOtherFee(exceptionFeeMapper.selectOtherFeeByAccountId(npb.getAccountPayId()));
                BigDecimal rate = npb.getRate();
                npb.setYcFeeRate(npb.getYcFee());
                npb.setTotalFeeRate(npb.getTotalFee());
                switch (npb.getUseRate()){
                    // 运输费-0，压车费-0
                    case "0" :
                        break;
                    // 运输费-0，压车费-1
                    case "1" :
                        npb.setYcFeeRate(npb.getYcFee().multiply(
                                new BigDecimal(1).subtract(
                                        rate.multiply(new BigDecimal(0.01)))
                        ).setScale(2, BigDecimal.ROUND_HALF_UP));
                        break;
                    // 运输费-1，压车费-0
                    case "2" :
                        npb.setTotalFeeRate(npb.getTotalFee().multiply(
                                new BigDecimal(1).subtract(
                                        rate.multiply(new BigDecimal(0.01)))
                        ).setScale(2, BigDecimal.ROUND_HALF_UP));
                        break;
                    // 运输费-1，压车费-1
                    case "3" :
                        npb.setYcFeeRate(npb.getYcFee().multiply(
                                new BigDecimal(1).subtract(
                                        rate.multiply(new BigDecimal(0.01)))
                        ).setScale(2, BigDecimal.ROUND_HALF_UP));
                        npb.setTotalFeeRate(npb.getTotalFee().multiply(
                                new BigDecimal(1).subtract(
                                        rate.multiply(new BigDecimal(0.01)))
                        ).setScale(2, BigDecimal.ROUND_HALF_UP));
                        break;
                }
                npb.setAllTotalFeeRate(npb.getTotalFeeRate().add(npb.getYcFeeRate().add(npb.getOtherFee())));
                npb.setSettlementStatus(getBillStatus(npb.getSettlementStatus()));
            }
        }
        query.setRecords(needPayBills);
        return query;

    }

    /**
     * 导出应付对账单列表
     *
     * @param financeQueryDTO 查询条件
     * @return
     */
    @Override
    public boolean needPayBllListForExcel(HttpServletResponse response, FinanceQueryDTO financeQueryDTO) {
        String[] idsArray;
        if (financeQueryDTO.getIds() != null && financeQueryDTO.getIds().length() > 0) {
            idsArray = Convert.toStrArray(financeQueryDTO.getIds());
            financeQueryDTO.setIdsArray(idsArray);
        } else {
            return false;
        }
        List<NeedPayBill> needPayBills = needPayBillMapper.needPayBillList(financeQueryDTO);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        for (NeedPayBill npb : needPayBills) {
            /*获取司机姓名*/
            DriverVO driverVO = getDriverByDriverId(npb.getDriverId(), financeQueryDTO.getTenantId());
            npb.setDriverName(driverVO != null ? driverVO.getDriverName() : "");
            /*日期范围处理*/
            String start = format.format(npb.getDateStart());
            String end = format.format(npb.getDateEnd());
            npb.setDateLimit(start + "~" + end);
            /*对账单状态处理*/
            if(npb.getSettlementStatus()!=null) {
                npb.setSettlementStatus(getBillStatus(npb.getSettlementStatus()));
            }
        }
        /*导出文件名*/
        String excelName = format.format(new Date()) + "应付对账单";
        ExcelUtil<NeedPayBill> util = new ExcelUtil<>(NeedPayBill.class);
        util.exportExcel(response, needPayBills, excelName, null);
        return true;

    }

    @Override
    public List<PhNeedPayDetail> selectPhBillToDZD(String[] orderIds, Integer tenantId) {
        List<PhNeedPayDetail> payDetails = needPayBillMapper.selectPhBillToDZD(orderIds, tenantId);
        for(PhNeedPayDetail ppd : payDetails) {
            for (int i = 0; i < ppd.getPickupAdds().size(); i++) {
                if (i == 0) {
                    ppd.setPickGoodsPlace(ppd.getPickupAdds().get(0).getAddressCity());
                }
            }
            for (int i = 0; i < ppd.getArrivalAdds().size(); i++) {
                if (i == ppd.getArrivalAdds().size() - 1) {
                    ppd.setSendGoodsPlace(ppd.getArrivalAdds().get(i).getAddressCity());
                }
            }
        }
        return payDetails;
    }

    @Override
    public List<CnNeedPayDetail> selectCnBillToDZD(String[] orderIds, Integer tenantId) {
        List<CnNeedPayDetail> cnNeedPayDetails = needPayBillMapper.selectCnBillToDZD(orderIds, tenantId);
        for(CnNeedPayDetail cnd : cnNeedPayDetails){
            for (int i = 0; i < cnd.getPickupAdds().size(); i++) {
                if (i == 0) {
                    cnd.setPickGoodsPlace(cnd.getPickupAdds().get(0).getAddressCity());
                }
            }
            for (int i = 0; i < cnd.getArrivalAdds().size(); i++) {
                if (i == cnd.getArrivalAdds().size() - 1) {
                    cnd.setSendGoodsPlace(cnd.getArrivalAdds().get(i).getAddressCity());
                }
            }
        }
        return cnNeedPayDetails;
    }

    @Override
    public NeedPayBill needPayBillList(String accountPayId, Integer tenantId) {
        NeedPayBill needPayBill = needPayBillMapper.needPayBillListByAid(accountPayId, tenantId);
        if(needPayBill != null){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            /*日期范围处理*/
            String start = format.format(needPayBill.getDateStart());
            String end = format.format(needPayBill.getDateEnd());
            needPayBill.setDateLimit(start + "~" + end);
            /*获取司机姓名*/
//            DriverVO driverVO = getDriverByDriverId(needPayBill.getDriverId(), tenantId);
//            needPayBill.setDriverName(driverVO != null ? driverVO.getDriverName(): "");
//            needPayBill.setDriverPhone(driverVO != null ? driverVO.getPhone(): "");
        }
        return needPayBill;
    }

    /*删除对账单后修改对应订单的状态*/
    @Override
    @Transactional
    public R<Boolean> updateOrderStatus(String[] accountPayIds, String login, Integer tenantId) {
        try {
            for (int i = 0; i < accountPayIds.length; i++) {
                /*删除  对账单*/
                NeedPayBill needPayBill = new NeedPayBill();
                needPayBill.setAccountPayId(accountPayIds[i]);
                needPayBill.setUpdateTime(new Date());
                needPayBill.setDelFlag(CommonConstant.STATUS_DEL);
                needPayBill.setUpdateBy(login);
                NeedPayBill param = new NeedPayBill();
                param.setAccountPayId(accountPayIds[i]);
                needPayBillMapper.update(needPayBill, new EntityWrapper<>(param));

                /*查询出对应的订单集合*/
                BillMiddle b = new BillMiddle();
                b.setAccountPayNumber(accountPayIds[i]);
                b.setTenantId(tenantId);
                List<BillMiddle> bs = billMiddleService.selectByAccountPayNumber(b);
                if(bs != null && bs.size()>0) {
                    String type = bs.get(0).getOrderNumber().substring(0, 2);
                    String[] orders = new String[bs.size()];
                    /*获取订单编号*/
                    for (int j = 0; j < bs.size(); j++) {
                        orders[j] = bs.get(j).getOrderNumber();
                    }
                    switch (type) {
                        case "PH":
                            /*更改订单状态*/
                            ordCommonTruckMapper.updateForYFBillByOrderIds(orders);
                            break;
                        case "CN":
                            /*更改订单状态*/
                            ordOrderMapper.removeForYFByOrderIds(orders, login);
                            break;
                    }
                }
                /*删除  对账单-订单  中间表*/
                BillMiddle billMiddle = new BillMiddle();
                billMiddle.setAccountPayNumber(accountPayIds[i]);
                billMiddleService.removeAllOrder(billMiddle);
            }
        }catch (Exception e) {
            //手动回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new R<>(Boolean.FALSE);
        }
        return new R<>(Boolean.TRUE);
    }

    @Override
    @Transactional
    public Boolean removeOrderFromAccount(String accountPayId, String orderId, Integer tenantId) {
            /*从中间表移除数据*/
            BillMiddle billMiddle = new BillMiddle();
            billMiddle.setOrderNumber(orderId);
            billMiddle.setAccountPayNumber(accountPayId);
            billMiddleService.removeOrder(billMiddle);
            /*获取对账单信息*/
            NeedPayBill needPayBill = needPayBillMapper.needPayBillListByAid(accountPayId, tenantId);
            /*需要移除的数据*/
            NeedPayBill removeBill = new NeedPayBill();

            String[] orders = {orderId};

            //不带利率的总费用
            BigDecimal totalFee = new BigDecimal(0);
            //带利率的总费用
            BigDecimal totalFeeByRate = new BigDecimal(0);
            BigDecimal etcFee = new BigDecimal(0);
            /*更改订单状态*/
            switch (orderId.substring(0, 2)) {
                case "PH":
                    /*订单派车状态*/
                    ordCommonTruckMapper.updateForYFBillByOrderIds(orders);
                    /*处理对账单数据*/
                    if (needPayBill != null) {
                        FinanceQueryDTO queryDTO = new FinanceQueryDTO();
                        queryDTO.setTenantId(tenantId);
                        List<PhNeedPayDetail> ppdList = needPayBillMapper.selectPhBillToDZD(orders, tenantId);
                        ppdList = caculatePhBill(ppdList, queryDTO);
                        //正常油卡费总计

                        //应付金额总计

                        //运费油卡总计

                        for (PhNeedPayDetail ppd : ppdList) {
                            totalFee = totalFee.add(ppd.getNeedPay());/*应付总额*/
                            totalFeeByRate = totalFeeByRate.add(ppd.getNeedPayByRate());/*应付总额(含税)*/
                            etcFee = etcFee.add(ppd.getEtcFee());
                        }
                        removeBill.setTotalFee(totalFee);
                        removeBill.setTotalFeeRate(totalFeeByRate);
                        removeBill.setEtcFee(etcFee);
                    }
                    break;
                case "CN":
                    /*订单派车状态*/
                    ordOrderMapper.removeForYFByOrderIds(orders, UserUtils.getUser());
                    if (needPayBill != null) {
                        FinanceQueryDTO queryDTO = new FinanceQueryDTO();
                        queryDTO.setTenantId(tenantId);
                        List<CnNeedPayDetail> cnpdList = needPayBillMapper.selectCnBillToDZD(orders, tenantId);
                        cnpdList = caculateCnBill(cnpdList, queryDTO);
                        for (CnNeedPayDetail cnpd : cnpdList) {
                            totalFee = totalFee.add(cnpd.getNeedPay());
                            totalFeeByRate = totalFeeByRate.add(cnpd.getNeedPayByRate());
                            etcFee = etcFee.add(cnpd.getEtcFee());
                        }
                        removeBill.setTotalFee(totalFee);
                        removeBill.setTotalFeeRate(totalFeeByRate);
                        removeBill.setEtcFee(etcFee);
                    }
                    break;
            }
            BigDecimal billNewTotal = needPayBill.getTotalFee().subtract(totalFee);
            BigDecimal etc = needPayBill.getEtcFee().subtract(etcFee);
            //BigDecimal billNewTotalRate = needPayBill.getTotalFeeRate().subtract(totalFeeByRate);
            //todo 正常油卡费相减
            needPayBill.setTotalFee(billNewTotal);
            needPayBill.setEtcFee(etc);
//        needPayBill.setTotalFeeRate(billNewTotalRate);
            /*删除已分配的金额数据*/
            if((needPayBill.getOrderAmount() - 1)==0){
                needPayBill.setDelFlag(CommonConstant.STATUS_DEL);
            }
            needPayBill.setOrderAmount(needPayBill.getOrderAmount() - 1);
            needPayBill.setPayCash(null);
            needPayBill.setNeedPayCash(null);
            needPayBill.setFreightOilcardFee(null);
            needPayBillMapper.updateById(needPayBill);
            return true;
    }

    public List<PhNeedPayDetail> caculatePhBill(List<PhNeedPayDetail> list, FinanceQueryDTO financeQueryDTO) {
        List<PhNeedPayDetail> result = new ArrayList<>();
        Integer tenantId = financeQueryDTO.getTenantId();
        for (PhNeedPayDetail ppd : list) {
               /*车主信息处理*/
            TruTruckOwnVo truTruckOwnVo = trunkFeign.getTruTruckOwnVoByPlateNumber(ppd.getPlateNumber(), tenantId);
            ppd.setTruckownName(truTruckOwnVo.getTruckownName());
            /********************异常费用列表*******************/
            List<OrdExceptionFee> exFeeList = exceptionfeeSet(ppd.getExceptionFeeList());
            for(OrdExceptionFee fee : exFeeList){
                if(fee.getExceptionFeeType().equals("压车费")){
                    ppd.setYcFee(ppd.getYcFee().add(fee.getExceptionFee()!=null?fee.getExceptionFee():new BigDecimal(0)));
                }
            }
            ppd.setExFee(ppd.getExFee().subtract(ppd.getYcFee()));
            ppd.setExceptionFeeList(exFeeList);
            //司机信息和税率
            DriverVO driverVO = getDriverByDriverId(ppd.getDriverId(), financeQueryDTO.getTenantId());
            ppd.setRateStr(getRateStr(ppd.getMoneyRate() != null ? ppd.getMoneyRate() : new BigDecimal(0)));
            ppd.setDriverName(driverVO.getDriverName() != null ? driverVO.getDriverName() : "");
            ppd.setDriverPhone(driverVO.getPhone() != null ? driverVO.getPhone() : "");
            //根据车牌号获取车辆类型
            TruckVO truckVO = getTruckByPlateNumebr(ppd.getPlateNumber(), financeQueryDTO.getTenantId());
            String carType = truckVO.getAttribute();
            ppd.setCarType(carType != null ? carType : "");
            /*获取结算方式
            if(("2").equals(carType)){
                ppd.setSettlement("单结");
            }else if(driverVO.getPayWay() == null){
                ppd.setSettlement("月结");
            }else {
                ppd.setSettlement(driverVO.getPayWay());
            }
            如果输入月结单结条件
            if(financeQueryDTO.getSettlement() != null){
                if(!financeQueryDTO.getSettlement().equals(ppd.getSettlement())){
                    continue;
                }
            }*/
            BigDecimal rate = ppd.getMoneyRate() != null ? ppd.getMoneyRate() : new BigDecimal(0);
            ppd.setSettlement(ppd.getSettlement().equals("0")?"单结":"月结");
            //计算总金额
            BigDecimal totalFee = new BigDecimal(0);
            totalFee = totalFee.add(ppd.getExFee())
                    .add(ppd.getYcFee())
                    .add(ppd.getTransportFee() != null ? ppd.getTransportFee() : new BigDecimal(0));/*运输费*/
            ppd.setYcFeeRate(ppd.getYcFee());
            ppd.setTransportFeeRate(ppd.getTransportFee());
            switch (ppd.getIsYFInvoice()){
                // 运输费-0，压车费-0
                case "0" :
                    break;
                // 运输费-0，压车费-1
                case "1" :
                    ppd.setYcFeeRate(ppd.getYcFee().multiply(
                            new BigDecimal(1).subtract(
                                    rate.multiply(new BigDecimal(0.01)))
                    ).setScale(2, BigDecimal.ROUND_HALF_UP));
                    break;
                // 运输费-1，压车费-0
                case "2" :
                    ppd.setTransportFeeRate(ppd.getTransportFee().multiply(
                            new BigDecimal(1).subtract(
                                    rate.multiply(new BigDecimal(0.01)))
                    ).setScale(2, BigDecimal.ROUND_HALF_UP));
                    break;
                // 运输费-1，压车费-1
                case "3" :
                    ppd.setYcFeeRate(ppd.getYcFee().multiply(
                            new BigDecimal(1).subtract(
                                    rate.multiply(new BigDecimal(0.01)))
                    ).setScale(2, BigDecimal.ROUND_HALF_UP));
                    ppd.setTransportFeeRate(ppd.getTransportFee().multiply(
                            new BigDecimal(1).subtract(
                                    rate.multiply(new BigDecimal(0.01)))
                    ).setScale(2, BigDecimal.ROUND_HALF_UP));
                    break;
            }
            //含利率的总费用
            ppd.setNeedPayByRate(ppd.getTransportFeeRate().add(ppd.getYcFeeRate().add(ppd.getExFee())));

            /*正常油卡费统计*/
            RechangeNormalVO vo = new RechangeNormalVO();
            vo.setTenantId(financeQueryDTO.getTenantId());
            vo.setIsPassed("1");
            vo.setTruckId(driverVO.getPlateId());
            /*判断结算方式*/
            if(ppd.getSettlement().equals("单结")){
                vo.setOrderId(ppd.getOrderId());
            }else {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                vo.setBeginTime(format.format(ppd.getStartTime()));
                vo.setEndTime(format.format(ppd.getEndTime()));
            }
            List<RechangeNormalVO> oilList = oilCardFeign.getRechangeNormalList(vo);
            BigDecimal rechargeSum = new BigDecimal("0");
            for(RechangeNormalVO oil : oilList){
                rechargeSum = rechargeSum.add(oil.getRechargeSum());
            }
            ppd.setCommonOilFee(rechargeSum);
            ppd.setNeedPay(totalFee);
            if(!ppd.getNeedPayStatus().equals("0")){ /*如果已分配*/
                //运费油卡 = 合计费用-分配现金
                if(ppd.getIsYFInvoice().equals("1")){
                    ppd.setNeedPay(ppd.getNeedPayByRate());
                }
            }
            /*地址处理*/
            String ways = "";
            String tiAdress="";
            String songAdress="";
            for(int i = 0; i < ppd.getPickupAdds().size(); i++){
                if(i != 0){
                    ways += ppd.getPickupAdds().get(i).getAddressCity()+",";
                }
                tiAdress+="-"+ppd.getPickupAdds().get(i).getAddressCity();
            }
            ppd.setPickGoodsPlace(tiAdress.replaceFirst("-",""));
            for(int i = 0; i < ppd.getArrivalAdds().size(); i++){
                if(i != ppd.getArrivalAdds().size()-1){
                    ways += ppd.getArrivalAdds().get(i).getAddressCity()+",";
                }
                songAdress+="-"+ppd.getPickupAdds().get(i).getAddressCity();
            }
            ppd.setSendGoodsPlace(songAdress.replaceFirst("-",""));
            ppd.setWaypoints(ways);
            result.add(ppd);
        }
        return result;
    }

    /**
     * 调账
     * @param orderId
     * @param transportFee
     * @param pickcnFee
     * @param oilFee
     * @param packFee
     * @param releaseFee
     * @return
     */
    @Override
    public Boolean updateBill(String orderId, BigDecimal transportFee, BigDecimal pickcnFee, BigDecimal oilFee, BigDecimal packFee, BigDecimal releaseFee) {
        try {
            if (orderId.substring(0,2).equals("PH")){
                OrdCommonTruck ordCommonTruck = new OrdCommonTruck();
                ordCommonTruck.setOrderId(orderId);
                ordCommonTruck.setOilFee(oilFee);
                ordCommonTruck.setTransportFee(transportFee);
                ordCommonTruck.setReleaseFee(releaseFee);
                ordCommonTruck.setPackFee(packFee);
                ordCommonTruckMapper.updateById(ordCommonTruck);
            }else {
                OrdOrder ordOrder = new OrdOrder();
                ordOrder.setOrderId(orderId);
                ordOrder.setNeedPay(transportFee);
                ordOrder.setPickcnFee(pickcnFee);
                ordOrder.setUpdateTime(new Date());
                OrdOrder condition = new OrdOrder();
                condition.setOrderId(orderId);
                ordOrderMapper.update(ordOrder,new EntityWrapper<>(condition));
            }
        }catch (Exception e){
            e.printStackTrace();
            return  Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    @Override
    @Transactional
    public Boolean updateNeedPayBill(UpdateForNeedPayBill orderBill){
        try {
            /*修改订单数据*/
            switch (orderBill.getOrderId().substring(0, 2)) {
                case "PH":
                    OrdCommonTruck commonTruck = new OrdCommonTruck();
                    commonTruck.setOrderId(orderBill.getOrderId());
                    if(orderBill.getTransportFee()!=null) {
                        commonTruck.setTransportFee(orderBill.getTransportFee());
                    }
                    if(orderBill.getDistance()!=null) {
                        commonTruck.setChargedMileage(orderBill.getDistance());
                    }
                    if(orderBill.getPayPrice()!=null){
                        commonTruck.setPayPrice(orderBill.getPayPrice());
                    }
                    if(orderBill.getMoneyRate()!=null){
                        commonTruck.setPayRate(orderBill.getMoneyRate());
                    }
                    commonTruck.setCash(new BigDecimal("0"));
                    commonTruck.setPayCash(new BigDecimal("0"));
                    commonTruck.setNeedPayStatus(CommonConstant.WFP);
                    commonTruck.setIsYFInvoice("0");
                    commonTruck.setEtcFee(new BigDecimal(0));
                    commonTruck.setOilPledge(new BigDecimal(0));
                    commonTruck.setTransOilFee(new BigDecimal(0));
                    ordCommonTruckMapper.updateById(commonTruck);
                    break;
                case "CN":
                    OrdOrder ordOrder = new OrdOrder();
                    ordOrder.setOrderId(orderBill.getOrderId());
                    if(orderBill.getDistance()!=null){
                        ordOrder.setKilometre(orderBill.getDistance());
                    }
                    if(orderBill.getTransportFee()!=null){
                        ordOrder.setNeedPay(orderBill.getTransportFee());
                    }
                    if(orderBill.getPayPrice()!=null){
                        ordOrder.setPayPrice(orderBill.getPayPrice());
                    }
                    if(orderBill.getMoneyRate()!=null){
                        ordOrder.setPayRate(orderBill.getMoneyRate());
                    }
                    ordOrder.setNeedPayStatus(CommonConstant.WFP);/*未分配状态*/
                    ordOrder.setPayCash(new BigDecimal("0"));
                    ordOrder.setCash(new BigDecimal("0"));
                    ordOrder.setIsYFInvoice("0");
                    ordOrder.setUpdateTime(new Date());
                    ordOrder.setUpdateBy(UserUtils.getUser());
                    ordOrder.setEtcFee(new BigDecimal(0));
                    ordOrder.setOilPledge(new BigDecimal(0));
                    ordOrder.setTransOilFee(new BigDecimal(0));
                    OrdOrder condition = new OrdOrder();
                    condition.setOrderId(orderBill.getOrderId());
                    ordOrderMapper.update(ordOrder, new EntityWrapper<>(condition));
                    break;
            }
            /*修改异常费用调账后的信息*/
            for (OrdExceptionFee fee : orderBill.getExceptionFeeList()) {
                fee.setUpdateBy(UserUtils.getUser());
                fee.setUpdateTime(new Date());
                exceptionFeeService.updateById(fee);
            }
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @Override
    public NeedPayBill selectFreightOilcardFeeByOrderId(String orderId, Integer id, Integer tenantId) {
        return needPayBillMapper.selectFreightOilcardFeeByOrderId(orderId, id, tenantId);
    }

    @Override
    public NeedPayBill selectBillByOrderId(String orderId, Integer tenantId) {
        return needPayBillMapper.selectBillByOrderId(orderId, tenantId);
    }

    @Override
    @Transactional
    public boolean rechargeCompleted(String orderId, String rechargeBy) {
        OrderSettlement settlement = settlementService.selectSettlementByOrderId(orderId);
        settlement.setOilStatus(2);
        settlement.setOilBy(rechargeBy);
        settlement.setOilTime(new Date());
        settlement.setOrderId(orderId);
        return settlementService.updateById(settlement);
    }

    @Override
    @Transactional
    public boolean setSettlementStatus(String orderId) {
        OrderSettlement settlement = settlementService.selectSettlementByOrderId(orderId);
        if(settlement != null){ //已存在结算数据
            settlement.setOilStatus(1);//设置状态为--充值中
            return settlementService.updateById(settlement);
        }else {
            settlement = new OrderSettlement();
            settlement.setOilStatus(1);
            settlement.setOrderId(orderId);
            return settlementService.insert(settlement);
        }
    }

    /**
     * 应付对账单号生成方法
     */
    public String getNeedPayableNumber(Integer tenantId) {
        String expensespayableNumber = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String str = format.format(new Date());
        expensespayableNumber = "YF";
        return expensespayableNumber + String.valueOf(tenantId) + str;
    }

    /**
     * 保存对账单预处理
     *
     * @param needPayBill
     * @param queryDTO
     * @return
     */
    public NeedPayBill preInsert(NeedPayBill needPayBill, FinanceQueryDTO queryDTO) {
        needPayBill.setTenantId(queryDTO.getTenantId());
        needPayBill.setDelFlag(CommonConstant.STATUS_NORMAL);
        needPayBill.setSettlementStatus("0");
        needPayBill.setCreateBy(UserUtils.getUser());
        needPayBill.setCreateTime(new Date());
        needPayBill.setDriverId(queryDTO.getDriverId());
        return needPayBill;
    }

    /*异常信息处理*/
    public List<OrdExceptionFee> exceptionfeeSet(List<OrdExceptionFee> list){
        if(list != null && list.size() > 0){
            List<SysDictVO> dictVOS = systemFeginServer.findDictByType("exception_fee_type", list.get(0).getTenantId());
            for(OrdExceptionFee fee : list){
                fee.setExceptionFeeType(getLable(dictVOS, fee.getExceptionFeeType()));
            }
        }
        return list;
    }

    public static String getLable(List<SysDictVO> dictVOS, String value){
        String lable = "";
        for(SysDictVO dict : dictVOS) {
            if(value.equals(dict.getValue())){
                lable = dict.getLabel();
                break;
            }
        }
        return lable;
    }

    public static String getBillStatus(String status){
        String billStatus = null;
        switch (status) {
            case CommonConstant.WFS:
                billStatus = "未发送";
                break;
            case CommonConstant.YFS:
                billStatus = "已发送";
                break;
            case CommonConstant.YFK:
                billStatus = "司机已反馈";
                break;
            case CommonConstant.YQR:
                billStatus = "司机已确认";
                break;
            case CommonConstant.YJS:
                billStatus = "已结算";
                break;
            case CommonConstant.WFP:
                billStatus = "未分配";
                break;
        }
        return billStatus;
    }

    @Override
    public List<NeedPayBill> truckAccountBillBaseInfo(Integer truckownId, String billStatus,Integer tenantId,String time,String driverName) {
		//根据司机姓名查出司机信息
		Integer driverId = 0;
		if (StrUtil.isNotBlank(driverName)) {
			TruDriver truDriver = trunkFeign.getDriverByName(driverName, tenantId);
			if (truDriver != null) {
				driverId = truDriver.getDriverId();
			}
		}
		String[] plateNumbers = getPlateNumbers(truckownId, tenantId);
        /*获取对账单列表*/
        FinanceQueryDTO dto = new FinanceQueryDTO();
        dto.setPlates(plateNumbers);
        dto.setDriverId(driverId == 0?null:driverId);
        dto.setStartTime(time);
        if(billStatus.equals("3")){
            String[] statuss = {"4","3"};
            dto.setStatusArray(statuss);
        }else{
            dto.setSettlementStatus(billStatus);
        }
        List<NeedPayBill> needPayBills = needPayBillMapper.needPayBillList(dto);
        for(NeedPayBill bill : needPayBills){
            List<OrderBill> orderBills = new ArrayList<>();
            /*获取 中间表 对应订单*/
            BillMiddle param = new BillMiddle();
            param.setAccountPayNumber(bill.getAccountPayId());
            param.setTenantId(bill.getTenantId());
            List<BillMiddle> billMiddles = billMiddleService.selectByAccountPayNumber(param);
            /*获取订单信息*/
            String orderIds[] = new String[billMiddles.size()];
            for(int i=0; i<billMiddles.size(); i++){
                orderIds[i] = billMiddles.get(i).getOrderNumber();
            }
            if(orderIds.length==0){
                return null;
            }
            /*根据订单编号查询*/
            switch (bill.getOrderType()){
                case "1":
                    bill.setOrderType("普货");
                    List<PhNeedPayDetail> ppdList = selectPhBillToDZD(orderIds, tenantId);
                    for(PhNeedPayDetail pd : ppdList){
                        OrderBill orderBill = new OrderBill();
                        orderBill.setDriverName(trunkFeign.getDriverByid(pd.getDriverId()).getDriverName());
                        orderBill.setDriverPhone(trunkFeign.getDriverByid(pd.getDriverId()).getPhone());
                        orderBill.setOrderId(pd.getOrderId());
                        orderBill.setOrderDate(pd.getStartTime());
                        //处理发货地
                        orderBill.setStartPlace(OrdExceptionConditionServiceImpl.getPlace(pd.getPickGoodsPlace()));
                        //处理到货地
                        orderBill.setEndPlace(OrdExceptionConditionServiceImpl.getPlace(pd.getSendGoodsPlace()));
                        // 获取异常费用信息
                        BigDecimal excepFee = new BigDecimal("0");
                        for(OrdExceptionFee fee : pd.getExceptionFeeList()){
                            excepFee = excepFee.add(fee.getExceptionFee());
                        }
                        /*异常费用*/
                        orderBill.setExFee(excepFee);
                        /*运输费*/
                        orderBill.setTransFee(pd.getTransportFee());
                        pd.setTransportFee(pd.getTransportFee()!=null?pd.getTransportFee():new BigDecimal(0));
                        BigDecimal totalFee = new BigDecimal("0");
                        totalFee = pd.getTransportFee().add(excepFee!=null?excepFee:new BigDecimal("0"));
                        orderBill.setTotalFee(totalFee);
                        orderBills.add(orderBill);
                    }
//                    bill.setPpdList(ppdList);
                    break;
                case "0":
                    bill.setOrderType("集装箱");
                    List<CnNeedPayDetail> cnpdList = selectCnBillToDZD(orderIds, tenantId);
                    for(CnNeedPayDetail pd : cnpdList){
                        OrderBill orderBill = new OrderBill();
                        orderBill.setDriverName(trunkFeign.getDriverByid(pd.getDriverId()).getDriverName());
                        orderBill.setDriverPhone(trunkFeign.getDriverByid(pd.getDriverId()).getPhone());
                        orderBill.setOrderId(pd.getOrderId());
                        orderBill.setOrderDate(pd.getType().equals("1")?pd.getStartTimeh():pd.getStartTimeq());
                        //处理发货地
                        orderBill.setStartPlace(OrdExceptionConditionServiceImpl.getPlace(pd.getPickGoodsPlace()));
                        //处理到货地
                        orderBill.setEndPlace(OrdExceptionConditionServiceImpl.getPlace(pd.getSendGoodsPlace()));
                        if(pd.getType().equals("0")){
                            orderBill.setStartPlace(pd.getPickCNPlace());
                        }else {
                            orderBill.setEndPlace(pd.getReturnCNPlace());
                        }
                        // 获取异常费用信息
                        BigDecimal excepFee = new BigDecimal("0");
                        for(OrdExceptionFee fee : pd.getExceptionFeeList()){
                            excepFee = excepFee.add(fee.getExceptionFee());
                        }
                        /*异常费用*/
                        orderBill.setExFee(excepFee);
                        orderBill.setTransFee(pd.getTransportFee());
                        pd.setTransportFee(pd.getTransportFee()!=null?pd.getTransportFee():new BigDecimal(0));
                        BigDecimal totalFee = new BigDecimal("0");
                        totalFee = pd.getTransportFee().add(excepFee!=null?excepFee:new BigDecimal("0"));
                        orderBill.setTotalFee(totalFee);
                        orderBills.add(orderBill);
                    }
//                    bill.setCnpdList(cnpdList);
                    break;
            }
            bill.setOrderBills(orderBills);
        }
        return needPayBills;
    }

    @Override
    public List<OrderBill> getTruckownBillBaseInfo(Integer truckownId, String orderId,String billStatus, Integer tenantId,String time,String driverName) {
    	//根据司机姓名查出司机信息
		Integer driverId = 0;
		if (StrUtil.isNotBlank(driverName)) {
			TruDriver truDriver = trunkFeign.getDriverByName(driverName, tenantId);
			if (truDriver != null) {
				driverId = truDriver.getDriverId();
			}
		}
		String[] plateNumbers = getPlateNumbers(truckownId, tenantId);

        if(billStatus.equals("3")){
            billStatus = billStatus + ",4";
        }
        if(billStatus.equals("4")){
            billStatus = billStatus + ",3";
        }
        String [] status = StrUtil.isNotBlank(billStatus)?billStatus.split(","):null;
        List<OrderBill> orderBills = needPayBillMapper.getTruckownBillBaseInfo(plateNumbers, orderId, status, tenantId,time,driverId);
        for (OrderBill bill : orderBills) {
            bill.setDriverName(trunkFeign.getDriverByid(bill.getDriverId()).getDriverName());
            bill.setDriverPhone(trunkFeign.getDriverByid(bill.getDriverId()).getPhone());
            bill.setYcFee(new BigDecimal(0));
            List<OrdExceptionFee> exFeeList = exceptionfeeSet(bill.getExceptionFeeList());
            for(OrdExceptionFee fee : exFeeList){
                if(fee.getExceptionFeeType().equals("压车费")){
                    bill.setYcFee(bill.getYcFee().add(fee.getExceptionFee()!=null?fee.getExceptionFee():new BigDecimal(0)));
                }
            }
            bill.setExFee(bill.getExFee().subtract(bill.getYcFee()));
            bill.setExceptionFeeList(exFeeList);
            //获取司机手机号姓名
//            DriverVO driverVO = getDriverByDriverId(driverId, tenantId);
//            TruckVO truckVO = getTruckByPlateNumebr(bill.getPlateNumber(), tenantId);
//            bill.setDriverName(driverVO.getDriverName() != null ? driverVO.getDriverName() : "");
//            bill.setDriverPhone(driverVO.getPhone() != null ? driverVO.getPhone() : "");
            //根据车牌号获取车辆类型
//            String carType = truckVO.getAttribute();
//            cnpd.setCarType(carType != null ? carType : "");
//            bill.setSettlement(bill.getSettlement().equals("0")?"单结":"月结");
            //计算总金额 = 异常费用+运输费
            BigDecimal totalFee = new BigDecimal(0);
            BigDecimal rate = bill.getPayRate() != null ? bill.getPayRate() : new BigDecimal(0);
            totalFee = totalFee.add(bill.getExFee())
                    .add(bill.getTransFee() != null ? bill.getTransFee() : new BigDecimal(0))
                    .add(bill.getYcFee());
            bill.setTransFeeRate(bill.getTransFee());
            bill.setYcFeeRate(bill.getYcFee());
            switch (bill.getIsYfInvoice()){
                // 运输费-0，压车费-0
                case "0" :
                    break;
                // 运输费-0，压车费-1
                case "1" :
                    bill.setYcFeeRate(bill.getYcFee().multiply(
                            new BigDecimal(1).subtract(
                                    rate.multiply(new BigDecimal(0.01)))
                    ).setScale(2, BigDecimal.ROUND_HALF_UP));
                    break;
                // 运输费-1，压车费-0
                case "2" :
                    bill.setTransFeeRate(bill.getTransFee().multiply(
                            new BigDecimal(1).subtract(
                                    rate.multiply(new BigDecimal(0.01)))
                    ).setScale(2, BigDecimal.ROUND_HALF_UP));
                    break;
                // 运输费-1，压车费-1
                case "3" :
                    bill.setYcFeeRate(bill.getYcFee().multiply(
                            new BigDecimal(1).subtract(
                                    rate.multiply(new BigDecimal(0.01)))
                    ).setScale(2, BigDecimal.ROUND_HALF_UP));
                    bill.setTransFeeRate(bill.getTransFee().multiply(
                            new BigDecimal(1).subtract(
                                    rate.multiply(new BigDecimal(0.01)))
                    ).setScale(2, BigDecimal.ROUND_HALF_UP));
                    break;
            }
            //含利率的总费用
            bill.setTotalFee(bill.getTransFeeRate().add(bill.getYcFeeRate().add(bill.getExFee())));

            //应付现金 = 分配现金-正常油卡费用

            /*正常油卡费*/
            bill.setCommonOilFee(bill.getTotalFee().subtract(bill.getPayCash()).subtract(bill.getTransOilFee()));

            /*地址处理*/
            bill.setStartPlace(bill.getPickupAdds().get(0).getAddressCity());
            bill.setEndPlace(bill.getArrivalAdds().get(bill.getArrivalAdds().size()-1).getAddressCity());
            if(bill.getType()!= null && bill.getType().equals("0")){
                bill.setStartPlace(bill.getPickCnPlace());
            }else if(bill.getType()!= null && bill.getType().equals("1")){
                bill.setEndPlace(bill.getReturnCnPlace());
            }
        }
        return orderBills;
    }

    @Override
    public BillBaseInfo getTruckownAccountBillBase(Integer truckownId,Integer tenantId) {
        return needPayBillMapper.getTruckownAccountBillBase(getPlateNumbers(truckownId,tenantId));
    }

    @Override
    public BillBaseInfo getTruckownBillNum(Integer truckownId,Integer tenantId) {
        return needPayBillMapper.getTruckownBillNum(getPlateNumbers(truckownId,tenantId));
    }

    public Integer getTruckId(List<TruckVO> truckVOS, String plateNumber){
        Integer truckId = null;
        for(TruckVO truckVO : truckVOS){
            if(truckVO.getPlateNumber().equals(plateNumber)){
                truckId = truckVO.getTruckId();
                break;
            }
        }
        return truckId;
    }

    //根据车主id查出车牌号
    public String[] getPlateNumbers(Integer truckownId,Integer tenantId){
        List<String> plateNumberList = trunkFeign.getPlateNumbersByTruckOwnId(truckownId, tenantId);
        if (plateNumberList.size() != 0) {
            String[] plateNumbers = plateNumberList.toArray(new String[plateNumberList.size()]);
            return plateNumbers;
        }else {
            String[] plateNumbers = {"0"};
            return plateNumbers;
        }
    }
}
