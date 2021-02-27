package com.zhkj.lc.order.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.order.dto.FinanceQueryDTO;
import com.zhkj.lc.order.dto.ReceiveBillCommonGoods;
import com.zhkj.lc.order.dto.ReceiveBillOrder;
import com.zhkj.lc.order.dto.ReceiveBillShortOrder;
import com.zhkj.lc.order.model.entity.Expensespayable;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 应收对账单表 Mapper 接口
 * </p>
 *
 * @author wzc
 * @since 2019-02-15
 */
public interface  ExpensespayableMapper extends BaseMapper<Expensespayable> {
    Integer deleteByIds(Expensespayable expensespayable);

    List<ReceiveBillOrder> selectCnOrderList(Query query, FinanceQueryDTO financeQueryDTO);

    List<ReceiveBillOrder> selectCnOrderList(FinanceQueryDTO financeQueryDTO);

    List<ReceiveBillCommonGoods> selectPhOrderList(Query query, FinanceQueryDTO financeQueryDTO);

    List<ReceiveBillCommonGoods> selectPhOrderList(FinanceQueryDTO financeQueryDTO);

    List<ReceiveBillShortOrder> selectPdOrderList(Query query, FinanceQueryDTO financeQueryDTO);

    BigDecimal countOrderTotalFee(@Param("orderIds")String[] orderIds,@Param("tenantId")Integer tenantId);

    BigDecimal countCommonGoodsTotalFee(@Param("orderIds")String[] orderIds,@Param("tenantId")Integer tenantId);

    BigDecimal countShortOrderTotalFee(@Param("orderIds")String[] orderIds,@Param("tenantId")Integer tenantId);

    Boolean SettlementProcess(Expensespayable expensespayable);

    List<Expensespayable> selectByIds(Expensespayable expensespayable);

    BigDecimal countTodayMoney(@Param("tenantId")Integer tenantId);

    List<Expensespayable> selectExpensespayableList(Query query, Expensespayable expensespayable);

    List<ReceiveBillOrder> selectCnByOrderIds(@Param("orderIds")String[] orderIds);

    List<ReceiveBillCommonGoods> selectPhByOrderIds(@Param("orderIds")String[] orderIds);

    List<ReceiveBillShortOrder> selectPdByOrderIds(@Param("orderIds")String[] orderIds);


    List<ReceiveBillOrder> selectCnOrderListByorderIds(Query query, FinanceQueryDTO financeQueryDTO);

    List<ReceiveBillCommonGoods> selectPhOrderListByorderIds(Query query, FinanceQueryDTO financeQueryDTO);

    Expensespayable selectByAccountPayId(@Param("accountPayId") String accountPayNumber);

    List<Expensespayable> selectExpensespayableByIds(Expensespayable expensespayable);

    ReceiveBillCommonGoods selectPhByOrderId(@Param("orderId")String orderId);

    ReceiveBillOrder selectCnByOrderId(@Param("orderId")String orderId);
}
