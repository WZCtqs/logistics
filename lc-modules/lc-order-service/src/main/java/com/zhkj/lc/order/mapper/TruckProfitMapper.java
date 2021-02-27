package com.zhkj.lc.order.mapper;

import com.zhkj.lc.order.dto.TruckProfit;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TruckProfitMapper {

    /**
     * 车辆近7天利润
     *
     * @param
     * @return
     */
    TruckProfit selectLast7daysProfit(@Param("driverId") Integer driverId, @Param("tenantId") Integer tenantId);

    BigDecimal selectLast7daysRate(@Param("driverId") Integer driverId, @Param("tenantId") Integer tenantId);

    /**
     * 车辆本月利润
     *
     * @param
     * @return
     */
    TruckProfit selectMonthdaysProfit(@Param("driverId") Integer driverId, @Param("tenantId") Integer tenantId);

    BigDecimal selectMonthdaysRate(@Param("driverId") Integer driverId, @Param("tenantId") Integer tenantId);

    /**
     * 车辆本季度利润
     *
     * @param
     * @return
     */
    TruckProfit selectCurrentSeasonProfit(@Param("driverId") Integer driverId, @Param("tenantId") Integer tenantId);

    BigDecimal selectCurrentSeasonRate(@Param("driverId") Integer driverId, @Param("tenantId") Integer tenantId);

    /**
     * 车辆近半年利润
     *
     * @param
     * @return
     */
    TruckProfit selectLast6MonthsProfit(@Param("driverId") Integer driverId, @Param("tenantId") Integer tenantId);

    BigDecimal selectLast6MonthsRate(@Param("driverId") Integer driverId, @Param("tenantId") Integer tenantId);

    /**
     * 车辆某一时间段利润
     *
     * @param
     * @return
     */
    TruckProfit selectSomeTimeProfit(TruckProfit truckProfit);

    BigDecimal selectSomeTimeRate(TruckProfit truckProfit);
}
