package com.zhkj.lc.oilcard.feign;

import com.zhkj.lc.oilcard.feign.impl.OrderFeignImpl;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description 订单相关接口
 * @Author ckj
 * @Date 2019/2/25 10:42
 */
@FeignClient(name = "lc-order-service", fallback = OrderFeignImpl.class)
public interface OrderFeign {

    @GetMapping("/ordOrder/selectByDriverIdForOil")
    String selectByDriverIdForOil(@RequestParam("driverId") Integer driverId, @RequestParam("tenantId") Integer tenantId);

    @GetMapping("/ordOrder/selectCountByDriver")
    Integer selectCountByDriver(@RequestParam("driverId") Integer driverId, @RequestParam("tenantId") Integer tenantId);

    @PostMapping("/needPayBill/setSettlementStatus/{orderId}")
	boolean setSettlementStatus(@PathVariable("orderId") String orderId);

    @PostMapping("/needPayBill/rechargeCompleted/{orderId}/{rechargeBy}")
	boolean rechargeCompleted(@PathVariable("orderId") String orderId, @PathVariable("rechargeBy") String rechargeBy);
}
