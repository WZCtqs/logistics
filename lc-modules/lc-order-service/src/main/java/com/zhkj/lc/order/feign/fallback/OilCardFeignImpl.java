package com.zhkj.lc.order.feign.fallback;

import com.zhkj.lc.common.util.R;
import com.zhkj.lc.common.vo.*;
import com.zhkj.lc.order.feign.OilCardFeign;
import com.zhkj.lc.order.feign.TrunkFeign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author cb
 * @description
 * @date 2018/12/5
 */
@Service
public class OilCardFeignImpl implements OilCardFeign {
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public List<RechangeNormalVO> getRechangeNormalList(RechangeNormalVO rechangeNormalVO) {
        return null;
    }

    @Override
    public BigDecimal selectOilFeeByDriverLast7days(FeeVO feeVO) {
        return null;
    }

    @Override
    public BigDecimal selectOilFeeByDriverMonthdays(FeeVO feeVO) {
        return null;
    }

    @Override
    public BigDecimal selectOilFeeByDriverCurrentSeason(FeeVO feeVO) {
        return null;
    }

    @Override
    public BigDecimal selectOilFeeByDriverLast6Months(FeeVO feeVO) {
        return null;
    }

    @Override
    public BigDecimal selectOilFeeByDriverSometime(FeeVO feeVO) {
        return null;
    }

    @Override
    public OilTruckRechargeVO findBalanceByTruckId(Integer truckId, Integer tenantId) {
        return null;
    }

    @Override
    public boolean add(OilTruckRechargeVO oilTruckRechargesVO) {
        return false;
    }
}
