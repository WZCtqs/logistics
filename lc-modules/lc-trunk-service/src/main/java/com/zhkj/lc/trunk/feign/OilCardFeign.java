package com.zhkj.lc.trunk.feign;

import com.zhkj.lc.trunk.feign.fallback.OilCardFeignImpl;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description 油卡外调接口
 * @Author ckj
 * @Date 2019/3/11 17:08
 */
@FeignClient(value = "lc-oilcard-service", fallback = OilCardFeignImpl.class)
public interface OilCardFeign {

    @GetMapping("/oilCard/countOilCardByTruckIds")
    Integer countOilCard(@RequestParam("driverIds") Integer[] driverIds, @RequestParam("tenantId") Integer tenantId);

    @GetMapping("/oilCard/driversCardNum")
    Integer driversCardNum(@RequestParam("driverId")Integer driverId, @RequestParam("tenantId") Integer tenantId);
}
