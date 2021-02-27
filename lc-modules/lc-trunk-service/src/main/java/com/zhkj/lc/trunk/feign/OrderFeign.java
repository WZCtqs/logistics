package com.zhkj.lc.trunk.feign;

import com.zhkj.lc.common.vo.DriverVO;
import com.zhkj.lc.trunk.feign.fallback.OrderFeignImpl;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Description 订单外调服务
 * @Author ckj
 * @Date 2019/3/11 17:47
 */
@FeignClient(value = "lc-order-service", fallback = OrderFeignImpl.class)
public interface OrderFeign {

    @GetMapping("/ordCommonGoods/selectProcOrdByDriverIds")
    Integer selectProcOrdByDriverIds(@RequestParam("driverIds") Integer[] driverIds, @RequestParam("tenantId") Integer tenantId);

    @GetMapping("/ordCommonGoods/getPlateNumberByProc/{tenantId}")
    List<DriverVO> selectPlateNumberByProc(@PathVariable("tenantId") Integer tenantId);
}
