package com.zhkj.lc.trunk.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.vo.FeeVO;
import com.zhkj.lc.common.vo.TruckTeamFeeVO;
import com.zhkj.lc.trunk.model.TruTruckFee;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author cb
 * @since 2019-02-11
 */
public interface ITruTruckFeeService extends IService<TruTruckFee> {

    Page selectTruckFeePage(Query objectQuery, Integer truckId,String feeMonth ,Integer tenantId);

    TruckTeamFeeVO selectTruckFeeByDriverLast7days(FeeVO feeVO);

    TruckTeamFeeVO selectTruckFeeByDriverMonthdays(FeeVO feeVO);

    TruckTeamFeeVO selectTruckFeeByDriverCurrentSeason(FeeVO feeVO);

    TruckTeamFeeVO selectTruckFeeByDriverLast6Months(FeeVO feeVO);

    TruckTeamFeeVO selectTruckFeeByDriverSometime(FeeVO feeVO);
}
