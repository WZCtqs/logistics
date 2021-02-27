package com.zhkj.lc.order.mapper;

import com.zhkj.lc.common.vo.DriverVO;
import com.zhkj.lc.order.dto.HeadOfficeProfit;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HeadOfficeProfitMapper {
    /**
     * 查询近7天
     * @return
     */
    HeadOfficeProfit selectLast7daysTeam(@Param("tenantId") Integer tenantId, @Param("list") List<DriverVO> list);

    HeadOfficeProfit selectLast7daysPerson(@Param("tenantId") Integer tenantId, @Param("list") List<DriverVO> list);

    HeadOfficeProfit selectLast7daysRate(@Param("tenantId") Integer tenantId,@Param("driverId") Integer driverId);

    /**
     * 查询本月
     * @param tenantId
     * @param list
     * @return
     */
    HeadOfficeProfit selectMonthdaysTeam(@Param("tenantId") Integer tenantId, @Param("list") List<DriverVO> list);

    HeadOfficeProfit selectMonthdaysPerson(@Param("tenantId") Integer tenantId, @Param("list") List<DriverVO> list);

    HeadOfficeProfit selectMonthdaysRate(@Param("tenantId") Integer tenantId,@Param("driverId") Integer driverId);

    /**
     * 查询本季度总公司利润
     * @return
     */
    HeadOfficeProfit selectCurrentSeasonTeam(@Param("tenantId") Integer tenantId, @Param("list") List<DriverVO> list);

    HeadOfficeProfit selectCurrentSeasonPerson(@Param("tenantId") Integer tenantId, @Param("list") List<DriverVO> list);

    HeadOfficeProfit selectCurrentSeasonRate(@Param("tenantId") Integer tenantId,@Param("driverId") Integer driverId);

    /**
     * 查询半年内总公司利润
     * @return
     */
    HeadOfficeProfit selectLast6MonthsTeam(@Param("tenantId") Integer tenantId, @Param("list") List<DriverVO> list);

    HeadOfficeProfit selectLast6MonthsPerson(@Param("tenantId") Integer tenantId, @Param("list") List<DriverVO> list);

    HeadOfficeProfit selectLast6MonthsRate(@Param("tenantId") Integer tenantId,@Param("driverId") Integer driverId);

    /**
     * 查询某段时间总公司利润
     * @return
     */
    HeadOfficeProfit selectSomeTimeTeam(HeadOfficeProfit headOfficeProfit);

    HeadOfficeProfit selectSomeTimePerson(HeadOfficeProfit headOfficeProfit);

    HeadOfficeProfit selectSomeTimeRate(HeadOfficeProfit headOfficeProfit);


}
