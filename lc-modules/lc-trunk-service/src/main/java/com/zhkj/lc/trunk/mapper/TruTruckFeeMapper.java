package com.zhkj.lc.trunk.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.vo.FeeVO;
import com.zhkj.lc.common.vo.TruckTeamFeeVO;
import com.zhkj.lc.trunk.model.TruTruckFee;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author cb
 * @since 2019-02-11
 */
public interface TruTruckFeeMapper extends BaseMapper<TruTruckFee> {

    List<TruTruckFee> selectTruckFeeList(Query<Object> objectQuery,
                                         @Param("truckId") Integer truckId,
                                         @Param("feeMonth") String feeMonth ,
                                         @Param("tenantId")Integer tenantId);

    TruckTeamFeeVO selectTruckFeeByDriverLast7days(FeeVO feeVO);

    TruckTeamFeeVO selectTruckFeeByDriverMonthdays(FeeVO feeVO);

    TruckTeamFeeVO selectTruckFeeByDriverCurrentSeason(FeeVO feeVO);

    TruckTeamFeeVO selectTruckFeeByDriverLast6Months(FeeVO feeVO);

    TruckTeamFeeVO selectTruckFeeByDriverSometime(FeeVO feeVO);
}
