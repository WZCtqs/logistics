package com.zhkj.lc.order.service;

import com.zhkj.lc.order.dto.TruckProfit;

import java.math.BigDecimal;
import java.util.List;

public interface TruckProfitService {
    /**
     * 车辆近7天利润
     * @param
     * @return
     */
    TruckProfit selectLast7daysProfit(Integer driverId,Integer tenantId);

    BigDecimal selectLast7daysRate(Integer driverId,Integer tenantId);

    /**
     * 车辆本月利润
     * @param
     * @return
     */
    TruckProfit selectMonthdaysProfit(Integer driverId,Integer tenantId);

    BigDecimal selectMonthdaysRate(Integer driverId,Integer tenantId);

    /**
     * 车辆本季度利润
     * @param
     * @return
     */
    TruckProfit selectCurrentSeasonProfit(Integer driverId,Integer tenantId);

    BigDecimal selectCurrentSeasonRate(Integer driverId,Integer tenantId);

    /**
     * 车辆近半年利润
     * @param
     * @return
     */
    TruckProfit selectLast6MonthsProfit(Integer driverId,Integer tenantId);

    BigDecimal selectLast6MonthsRate(Integer driverId,Integer tenantId);

    /**
     * 车辆某一时间段利润
     * @param
     * @return
     */
    TruckProfit selectSomeTimeProfit(TruckProfit truckProfit);

    BigDecimal selectSomeTimeRate(TruckProfit truckProfit);
}
