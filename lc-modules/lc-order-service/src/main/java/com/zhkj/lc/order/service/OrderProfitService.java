package com.zhkj.lc.order.service;

import com.zhkj.lc.order.dto.HeadOfficeProfit;
import com.zhkj.lc.order.dto.OrderProfit;
import com.zhkj.lc.order.dto.TruckProfit;

import java.util.List;

public interface OrderProfitService {
    /**
     * 订单利润
     * @param orderProfit
     * @return
     */
    List<OrderProfit> selectLast7daysOrderProfit(OrderProfit orderProfit);
    /**
     * 车辆利润
     * @param orderProfit
     * @return
     */
    List<TruckProfit> selectTruckOrderProfit(OrderProfit orderProfit);

    /**
     * 总利润利润
     * @param orderProfit
     * @return
     */
    HeadOfficeProfit selectSumProfit(OrderProfit orderProfit);
}
