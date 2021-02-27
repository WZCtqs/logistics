package com.zhkj.lc.order.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.R;
import com.zhkj.lc.order.dto.*;
import com.zhkj.lc.order.model.entity.NeedPayBill;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author cb
 * @since 2019-02-19
 */
public interface INeedPayBillService extends IService<NeedPayBill> {

    Page<NeedPayBaseModel> getCnNeedPayPage(Query query, FinanceQueryDTO financeQueryDTO);

    Page<NeedPayBaseModel> getPhNeedPayPage(Query<Object> objectQuery, FinanceQueryDTO financeQueryDTO);

    Page<NeedPayBaseModel> getPdNeedPayPage(Query<Object> objectQuery, FinanceQueryDTO financeQueryDTO);

    BigDecimal getTotalFee(FinanceQueryDTO financeQueryDTO);

    Boolean sendBill(String orderId,String plateNumber, Integer tenantId);

    List<OrderBill> getBillBaseInfo(Integer driverId, String orderId, String billStatus, Integer tenantId);

    List<NeedPayBill> accountBillBaseInfo(Integer driverId, String billStatus,Integer tenantId);

    Boolean confirmBill(String orderId);

    Boolean confirm(Integer id, String accountPayId, String flag, String repaynumber, Integer tenantId, String loginUser);

    Boolean feedback(String orderId, String feedMsg);

    Boolean cancleFeedback(String orderId);

    Boolean designCash(String orderId,String ifInvoice, BigDecimal payCash, BigDecimal cash, BigDecimal etcFee, BigDecimal oilPledge,BigDecimal transOilFee, BigDecimal rate);

    Boolean pcConfirmBill(String orderId,String repaynumber, Integer tenantId, String loginUser);

    NeedPayBaseModel getBillDetailInfoForPc(String orderId,Integer tenantId);

    Boolean exportBillByQuery(HttpServletRequest request, HttpServletResponse response, FinanceQueryDTO queryDTO);
    /*应付对账单页面接口*/
    Page<NeedPayBill> needPayBillList(Query query, FinanceQueryDTO financeQueryDTO);

    boolean needPayBllListForExcel(HttpServletResponse response, FinanceQueryDTO financeQueryDTO);

    List<PhNeedPayDetail>selectPhBillToDZD(String[] orderIds, Integer tenantId);

    List<CnNeedPayDetail>selectCnBillToDZD(String[] orderIds, Integer tenantId);

    NeedPayBill needPayBillList(String accountPayId, Integer tenantId);

    R<Boolean> updateOrderStatus(String[] accountPayId, String login, Integer tenantId);

    Boolean removeOrderFromAccount(String accountPayId, String orderId, Integer tenantId);

    Boolean addToDZD(FinanceQueryDTO queryDTO);

    Boolean updateBill(String orderId, BigDecimal transportFee, BigDecimal pickcnFee, BigDecimal oilFee, BigDecimal packFee, BigDecimal releaseFee);

    Boolean updateNeedPayBill(UpdateForNeedPayBill orderList);

    NeedPayBill selectFreightOilcardFeeByOrderId(String orderId, Integer id, Integer tenantId);

    NeedPayBill selectBillByOrderId(String orderId, Integer tenantId);

    boolean rechargeCompleted(String orderId, String rechargeBy);

    boolean setSettlementStatus(String orderId);



    List<NeedPayBill> truckAccountBillBaseInfo(Integer truckownId, String billStatus,Integer tenantId,String time,String driverName);

    BillBaseInfo getTruckownAccountBillBase(Integer truckownId,Integer tenantId);

    BillBaseInfo getTruckownBillNum(Integer truckownId,Integer tenantId);

    List<OrderBill> getTruckownBillBaseInfo(Integer truckownId, String orderId, String billStatus, Integer tenantId,String time ,String driverName);
}
