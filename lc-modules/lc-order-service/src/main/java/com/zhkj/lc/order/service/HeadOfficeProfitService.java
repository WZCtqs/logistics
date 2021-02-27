package com.zhkj.lc.order.service;

import com.zhkj.lc.common.vo.DriverVO;
import com.zhkj.lc.order.dto.HeadOfficeProfit;

import java.util.List;

public interface HeadOfficeProfitService {
    /**
     * 查询近7天内总公司利润
     * @return
     */
    HeadOfficeProfit selectLast7daysTeam(Integer tenantId, List<DriverVO> list);

    HeadOfficeProfit selectLast7daysPerson(Integer tenantId, List<DriverVO> list);

    HeadOfficeProfit selectLast7daysRate(Integer tenantId, Integer driverId);

    /**
     * 查询本月总公司利润
     * @return
     */
    HeadOfficeProfit selectMonthdaysTeam(Integer tenantId, List<DriverVO> list);

    HeadOfficeProfit selectMonthdaysPerson(Integer tenantId, List<DriverVO> list);

    HeadOfficeProfit selectMonthdaysRate(Integer tenantId, Integer driverId);

    /**
     * 查询本季度总公司利润
     * @return
     */
    HeadOfficeProfit selectCurrentSeasonTeam(Integer tenantId, List<DriverVO> list);

    HeadOfficeProfit selectCurrentSeasonPerson(Integer tenantId, List<DriverVO> list);

    HeadOfficeProfit selectCurrentSeasonRate(Integer tenantId, Integer driverId);

    /**
     * 查询半年内总公司利润
     * @return
     */
    HeadOfficeProfit selectLast6MonthsTeam(Integer tenantId,List<DriverVO> list);

    HeadOfficeProfit selectLast6MonthsPerson(Integer tenantId,List<DriverVO> list);

    HeadOfficeProfit selectLast6MonthsRate(Integer tenantId,Integer driverId);

    /**
     * 查询某段时间总公司利润
     * @return
     */
    HeadOfficeProfit selectSomeTimeTeam(HeadOfficeProfit headOfficeProfit);

    HeadOfficeProfit selectSomeTimePerson(HeadOfficeProfit headOfficeProfit);

    HeadOfficeProfit selectSomeTimeRate(HeadOfficeProfit headOfficeProfit);


}
