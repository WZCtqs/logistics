package com.zhkj.lc.trunk.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.vo.FeeVO;
import com.zhkj.lc.common.vo.TruckTeamFeeVO;
import com.zhkj.lc.trunk.mapper.TruTruckFeeMapper;
import com.zhkj.lc.trunk.model.TruTruckFee;
import com.zhkj.lc.trunk.service.ITruTruckFeeService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author cb
 * @since 2019-02-11
 */
@Service
public class TruTruckFeeServiceImpl extends ServiceImpl<TruTruckFeeMapper, TruTruckFee> implements ITruTruckFeeService {


    @Autowired
    private TruTruckFeeMapper truTruckFeeMapper;
    @Override
    public Page selectTruckFeePage(Query  objectQuery, Integer truckId,String feeMonth, Integer tenantId) {
        List<TruTruckFee> list = truTruckFeeMapper.selectTruckFeeList(objectQuery,truckId,feeMonth, tenantId);
        objectQuery.setRecords(list);
        return objectQuery;
    }

    @Override
    public TruckTeamFeeVO selectTruckFeeByDriverLast7days(FeeVO feeVO) {
        return truTruckFeeMapper.selectTruckFeeByDriverLast7days(feeVO);
    }

    @Override
    public TruckTeamFeeVO selectTruckFeeByDriverMonthdays(FeeVO feeVO) {
        return truTruckFeeMapper.selectTruckFeeByDriverMonthdays(feeVO);
    }

    @Override
    public TruckTeamFeeVO selectTruckFeeByDriverCurrentSeason(FeeVO feeVO) {
        return truTruckFeeMapper.selectTruckFeeByDriverCurrentSeason(feeVO);
    }

    @Override
    public TruckTeamFeeVO selectTruckFeeByDriverLast6Months(FeeVO feeVO) {
        return truTruckFeeMapper.selectTruckFeeByDriverLast6Months(feeVO);
    }

    @Override
    public TruckTeamFeeVO selectTruckFeeByDriverSometime(FeeVO feeVO) {
        return truTruckFeeMapper.selectTruckFeeByDriverSometime(feeVO);
    }
}
