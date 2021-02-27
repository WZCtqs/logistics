package com.zhkj.lc.order.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.R;
import com.zhkj.lc.order.dto.FinanceQueryDTO;
import com.zhkj.lc.order.dto.ReceiveBillCommonGoods;
import com.zhkj.lc.order.dto.ReceiveBillOrder;
import com.zhkj.lc.order.dto.ReceiveBillShortOrder;
import com.zhkj.lc.order.model.entity.Expensespayable;
import com.zhkj.lc.order.model.entity.OrdCommonGoods;
import com.zhkj.lc.order.model.entity.OrdOrder;
import com.zhkj.lc.order.model.entity.OrdShortOrder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 应收对账单表 服务类
 * </p>
 *
 * @author wzc
 * @since 2019-02-15
 */
public interface IExpensespayableService extends IService<Expensespayable> {

    Boolean deleteByIds(String ids);

    Page selectCnOrderList(Query query, FinanceQueryDTO financeQueryDTO);

    Page selectPhOrderList(Query query, FinanceQueryDTO financeQueryDTO);

    Page selectPdOrderList(Query query, FinanceQueryDTO financeQueryDTO);

    BigDecimal getTotalFee(FinanceQueryDTO financeQueryDTO);

    BigDecimal countOrderTotalFee(String[] orderIds,Integer tenantId);

    BigDecimal countCommonGoodsTotalFee(String[] orderIds,Integer tenantId);

    BigDecimal countShortOrderTotalFee(String[] orderIds,Integer tenantId);

    Boolean SettlementProcess(Expensespayable expensespayable);

    List<Expensespayable> selectByIds(String ids);

    BigDecimal countTodayMoney(Integer tenantId);

    Boolean exportBillByQuery(HttpServletRequest request, HttpServletResponse response, FinanceQueryDTO queryDTO);

    Boolean exportExpensespayable(HttpServletRequest request, HttpServletResponse response, Expensespayable expensespayable);

    Boolean exportExpensespayableByBill(HttpServletRequest request, HttpServletResponse response, Expensespayable expensespayable);

    Page selectExpensespayableList(Query query, Expensespayable expensespayable);

    List<ReceiveBillOrder> selectCnByOrderIds(String[] orderIds);

    List<ReceiveBillCommonGoods> selectPhByOrderIds(String[] orderIds);

    List<ReceiveBillShortOrder> selectPdByOrderIds(String[] orderIds);

    Expensespayable selectByAccountPayId(String accountPayNumber);

    ReceiveBillCommonGoods selectPhByOrderId(String orderId);

    ReceiveBillOrder selectCnByOrderId(String orderId);
}
