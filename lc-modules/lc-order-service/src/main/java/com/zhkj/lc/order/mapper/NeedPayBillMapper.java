package com.zhkj.lc.order.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.order.dto.*;
import com.zhkj.lc.order.model.entity.NeedPayBill;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author cb
 * @since 2019-02-19
 */
@Repository
public interface NeedPayBillMapper extends BaseMapper<NeedPayBill> {

    /*分页查询*/
    List<CnNeedPayDetail> selectCnNeedPayList(Query query,FinanceQueryDTO financeQueryDTO);
    /*分页查询*/
    List<PhNeedPayDetail> selectPhNeedPayList(Query query, FinanceQueryDTO financeQueryDTO);
    /*不分页查询*/
    List<CnNeedPayDetail> selectCnNeedPayList(FinanceQueryDTO financeQueryDTO);
    /*不分页查询*/
    List<PhNeedPayDetail> selectPhNeedPayList(FinanceQueryDTO financeQueryDTO);

    List<PdNeedPayDetail> selectPdNeedPayList(Query query, FinanceQueryDTO financeQueryDTO);

    BillBaseInfo getBillNum(Integer driverId);

    BillBaseInfo getAccountBillNum(Integer driverId);

    List<OrderBill> getBillBaseInfo(@Param("driverId") Integer driverId,
                                    @Param("orderId") String orderId,
                                    @Param("status") String[] status,
                                    @Param("tenantId") Integer tenantId);

    BillDetailForPC selectCNBillDetailPC(@Param("orderId") String orderId, @Param("tenantId") Integer tenantId);

    BillDetailForPC selectPHBillDetailPC(@Param("orderId") String orderId, @Param("tenantId") Integer tenantId);

    List<NeedPayBill> needPayBillList(Query query,FinanceQueryDTO financeQueryDTO);

    List<NeedPayBill> needPayBillList(FinanceQueryDTO financeQueryDTO);

    NeedPayBill needPayBillListByAid(@Param("accountPayId")String accountPayId, @Param("tenantId") Integer tenantId);

    List<PhNeedPayDetail> selectPhBillToDZD(@Param("orderIds")String []orderIds, @Param("tenantId") Integer tenantId);

    List<CnNeedPayDetail> selectCnBillToDZD(@Param("orderIds") String[] orderIds, @Param("tenantId") Integer tenantId);

    NeedPayBill selectFreightOilcardFeeByOrderId(@Param("orderId") String orderId, @Param("id") Integer id, @Param("tenantId") Integer tenantId);

    NeedPayBill selectBillByOrderId(@Param("orderId") String orderId, @Param("tenantId") Integer tenantId);


    BillBaseInfo getTruckownAccountBillBase(@Param("plateNumbers")String[] plateNumbers);

    BillBaseInfo getTruckownBillNum(@Param("plateNumbers")String[] plateNumbers);

    List<OrderBill> getTruckownBillBaseInfo(@Param("plateNumbers")String[] plateNumbers, @Param("orderId") String orderId,
                                            @Param("status") String[] status, @Param("tenantId") Integer tenantId,@Param("time")String time,@Param("driverId")Integer driverId);
}
