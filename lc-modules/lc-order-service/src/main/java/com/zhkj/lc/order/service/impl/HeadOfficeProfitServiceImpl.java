package com.zhkj.lc.order.service.impl;

import com.zhkj.lc.common.vo.DriverVO;
import com.zhkj.lc.order.dto.HeadOfficeProfit;
import com.zhkj.lc.order.mapper.HeadOfficeProfitMapper;
import com.zhkj.lc.order.service.HeadOfficeProfitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HeadOfficeProfitServiceImpl implements HeadOfficeProfitService {
    @Autowired
    private HeadOfficeProfitMapper headOfficeProfitMapper;
    /**
     * 查询近7天内总公司利润
     * @return
     */
    @Override
    public HeadOfficeProfit selectLast7daysTeam(Integer tenantId, List<DriverVO> list){
        return headOfficeProfitMapper.selectLast7daysTeam(tenantId,list);
    }

    @Override
    public HeadOfficeProfit selectLast7daysPerson(Integer tenantId, List<DriverVO> list){
        return headOfficeProfitMapper.selectLast7daysPerson(tenantId,list);
    }

    @Override
    public HeadOfficeProfit selectLast7daysRate(Integer tenantId, Integer driverId) {
        return headOfficeProfitMapper.selectLast7daysRate(tenantId,driverId);
    }

    /**
     * 查询本月总公司利润
     * @return
     */
    @Override
    public HeadOfficeProfit selectMonthdaysTeam(Integer tenantId, List<DriverVO> list) {
        return headOfficeProfitMapper.selectMonthdaysTeam(tenantId,list);
    }

    @Override
    public HeadOfficeProfit selectMonthdaysPerson(Integer tenantId, List<DriverVO> list) {
        return headOfficeProfitMapper.selectMonthdaysPerson(tenantId,list);
    }

    @Override
    public HeadOfficeProfit selectMonthdaysRate(Integer tenantId, Integer driverId) {
        return headOfficeProfitMapper.selectMonthdaysRate(tenantId,driverId);
    }

    /**
     * 查询本季度总公司利润
     * @return
     */
    @Override
    public HeadOfficeProfit selectCurrentSeasonTeam(Integer tenantId, List<DriverVO> list) {
        return headOfficeProfitMapper.selectCurrentSeasonTeam(tenantId,list);
    }

    @Override
    public HeadOfficeProfit selectCurrentSeasonPerson(Integer tenantId, List<DriverVO> list) {
        return headOfficeProfitMapper.selectCurrentSeasonPerson(tenantId,list);
    }

    @Override
    public HeadOfficeProfit selectCurrentSeasonRate(Integer tenantId, Integer driverId) {
        return headOfficeProfitMapper.selectCurrentSeasonRate(tenantId,driverId);
    }

    /**
     * 查询半年内总公司利润
     * @return
     */

    @Override
    public HeadOfficeProfit selectLast6MonthsTeam(Integer tenantId, List<DriverVO> list) {
        return headOfficeProfitMapper.selectLast6MonthsTeam(tenantId,list);
    }

    @Override
    public HeadOfficeProfit selectLast6MonthsPerson(Integer tenantId, List<DriverVO> list) {
        return headOfficeProfitMapper.selectLast6MonthsPerson(tenantId,list);
    }

    @Override
    public HeadOfficeProfit selectLast6MonthsRate(Integer tenantId, Integer driverId) {
        return headOfficeProfitMapper.selectLast6MonthsRate(tenantId,driverId);
    }

    /**
     * 查询某段时间总公司利润
     * @return
     */
    @Override
    public HeadOfficeProfit selectSomeTimeTeam(HeadOfficeProfit headOfficeProfit) {
        return headOfficeProfitMapper.selectSomeTimeTeam(headOfficeProfit);
    }

    @Override
    public HeadOfficeProfit selectSomeTimePerson(HeadOfficeProfit headOfficeProfit) {
        return headOfficeProfitMapper.selectSomeTimePerson(headOfficeProfit);
    }

    @Override
    public HeadOfficeProfit selectSomeTimeRate(HeadOfficeProfit headOfficeProfit) {
        return headOfficeProfitMapper.selectSomeTimeRate(headOfficeProfit);
    }


}
