package com.zhkj.lc.order.feign;

import com.zhkj.lc.common.vo.*;
import com.zhkj.lc.order.feign.fallback.OilCardFeignImpl;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;


@FeignClient(value = "lc-oilcard-service" ,fallback = OilCardFeignImpl.class)
public interface OilCardFeign {

    /*查询司机列表*/
    @PostMapping("/rechargeNormal/list")
    List<RechangeNormalVO> getRechangeNormalList(@RequestBody  RechangeNormalVO rechangeNormalVO);

    @PostMapping("/oilRecharge/selectOilFeeByDriverLast7days")
    BigDecimal selectOilFeeByDriverLast7days(FeeVO feeVO);

    @PostMapping("/oilRecharge/selectOilFeeByDriverMonthdays")
    BigDecimal selectOilFeeByDriverMonthdays(FeeVO feeVO);

    @PostMapping("/oilRecharge/selectOilFeeByDriverCurrentSeason")
    BigDecimal selectOilFeeByDriverCurrentSeason(FeeVO feeVO);

    @PostMapping("/oilRecharge/selectOilFeeByDriverLast6Months")
    BigDecimal selectOilFeeByDriverLast6Months(FeeVO feeVO);

    @PostMapping("/oilRecharge/selectOilFeeByDriverSometime")
    BigDecimal selectOilFeeByDriverSometime(FeeVO feeVO);

    @GetMapping("/oilTruckRecharges/getBalance/{truckId}/{tenantId}")
    OilTruckRechargeVO findBalanceByTruckId(@PathVariable("truckId") Integer truckId,@PathVariable("tenantId")Integer tenantId);

    @PostMapping("/oilTruckRecharges/add/feign")
    boolean add(@RequestBody OilTruckRechargeVO oilTruckRechargesVO);
}
