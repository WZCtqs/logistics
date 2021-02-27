package com.zhkj.lc.order.mapper;

import com.zhkj.lc.order.dto.OrderProfit;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderProfitMapper {
    /**
     * 订单近7天利润
     * @param orderProfit
     * @return
     */
    List<OrderProfit> selectLast7daysOrderProfit(OrderProfit orderProfit);

    /**
     * 订单本月利润
     * @param orderProfit
     * @return
     */
    List<OrderProfit> selectMonthdaysOrderProfit(OrderProfit orderProfit);

    /**
     * 订单本季度利润
     * @param orderProfit
     * @return
     */
    List<OrderProfit> selectCurrentSeasonOrderProfit(OrderProfit orderProfit);

    /**
     * 订单近半年利润
     * @param orderProfit
     * @return
     */
    List<OrderProfit> selectLast6MonthsOrderProfit(OrderProfit orderProfit);

    /**
     * 订单某段时间内利润
     * @param orderProfit
     * @return
     */
    List<OrderProfit> selectSomeTimeOrderProfit(OrderProfit orderProfit);
}
