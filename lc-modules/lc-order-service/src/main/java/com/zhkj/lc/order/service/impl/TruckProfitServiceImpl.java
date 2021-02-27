package com.zhkj.lc.order.service.impl;

import com.zhkj.lc.order.dto.TruckProfit;
import com.zhkj.lc.order.mapper.TruckProfitMapper;
import com.zhkj.lc.order.service.TruckProfitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TruckProfitServiceImpl implements TruckProfitService {
    @Autowired
    private TruckProfitMapper truckProfitMapper;


    @Override
    public TruckProfit selectLast7daysProfit(Integer driverId, Integer tenantId) {
        return truckProfitMapper.selectLast7daysProfit(driverId,tenantId);
    }

    @Override
    public BigDecimal selectLast7daysRate(Integer driverId, Integer tenantId) {
        return truckProfitMapper.selectLast7daysRate(driverId, tenantId);
    }

    @Override
    public TruckProfit selectMonthdaysProfit(Integer driverId, Integer tenantId) {
        return truckProfitMapper.selectMonthdaysProfit(driverId, tenantId);
    }

    @Override
    public BigDecimal selectMonthdaysRate(Integer driverId, Integer tenantId) {
        return truckProfitMapper.selectMonthdaysRate(driverId, tenantId);
    }

    @Override
    public TruckProfit selectCurrentSeasonProfit(Integer driverId, Integer tenantId) {
        return truckProfitMapper.selectCurrentSeasonProfit(driverId, tenantId);
    }

    @Override
    public BigDecimal selectCurrentSeasonRate(Integer driverId, Integer tenantId) {
        return truckProfitMapper.selectCurrentSeasonRate(driverId, tenantId);
    }

    @Override
    public TruckProfit selectLast6MonthsProfit(Integer driverId, Integer tenantId) {
        return truckProfitMapper.selectLast6MonthsProfit(driverId, tenantId);
    }

    @Override
    public BigDecimal selectLast6MonthsRate(Integer driverId, Integer tenantId) {
        return truckProfitMapper.selectLast6MonthsRate(driverId, tenantId);
    }

    @Override
    public TruckProfit selectSomeTimeProfit(TruckProfit truckProfit) {
        return truckProfitMapper.selectSomeTimeProfit(truckProfit);
    }

    @Override
    public BigDecimal selectSomeTimeRate(TruckProfit truckProfit) {
        return truckProfitMapper.selectSomeTimeRate(truckProfit);
    }
}
