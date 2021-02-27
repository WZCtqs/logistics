package com.zhkj.lc.oilcard.feign;

import com.zhkj.lc.common.util.R;
import com.zhkj.lc.common.vo.AnnouncementVO;
import com.zhkj.lc.common.vo.DriverVO;
import com.zhkj.lc.common.vo.TruckVO;
import com.zhkj.lc.oilcard.feign.impl.TruckFeignImpl;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "lc-truck-service",fallback = TruckFeignImpl.class)
public interface TruckFeign {

    @GetMapping(value="/truTruck/selectTruckById")
    TruckVO getTruckByid(@RequestParam("id") Integer id);

    @PostMapping(value="/truTruck/selectPlateNumberLists")
    List<TruckVO> listTruck(@RequestBody TruckVO truckVO);

    @GetMapping(value="/truDriver/selectDriverById")
    DriverVO getDriverByid(@RequestParam("id") Integer id);

    @PutMapping("/truDriver")
    R<Boolean> edit(@RequestBody DriverVO driverVO);

    @PostMapping("/truDriver/selectPlateNumberByplateId")
    List<DriverVO> selectDriverByplateId(@RequestBody DriverVO driverVO);

    @PostMapping("/truDriver/selectAllDrivers")
    List<DriverVO> selectAllDrivers(@RequestBody DriverVO driverVO);

    /** 获取司机所属车队结算方式 */
    @GetMapping("/truDriver/getDriverPayType")
    String getDriverPayType(@RequestParam("driverId") Integer driverId, @RequestParam("tenantId") Integer tenantId);

    @PostMapping("announcement/add/feign")
    R<Boolean> addFeign(@RequestBody AnnouncementVO announcementVO);
}
