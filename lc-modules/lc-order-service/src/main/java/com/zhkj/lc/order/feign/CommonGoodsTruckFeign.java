package com.zhkj.lc.order.feign;


import com.zhkj.lc.common.vo.CustomerVO;
import com.zhkj.lc.common.vo.DriverVO;
import com.zhkj.lc.common.vo.TruckTeamVo;
import com.zhkj.lc.common.vo.TruckVO;
import com.zhkj.lc.order.feign.fallback.CommonGoodsTruckFeignImpl;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "lc-truck-service" ,fallback = CommonGoodsTruckFeignImpl.class)
public interface CommonGoodsTruckFeign {


    @GetMapping("/truTruck/selectTruckIdBy")
    Integer selectTruckIdBy(@RequestParam("driverId") Integer driverId, @RequestParam("plateNumber") String plateNumber);

    /**
     * 获取所有自有车的车辆信息
     */
    @PostMapping("/truTruck/selectPlateNumberByAttribute")
     List<TruckVO> getZYTruckList(@RequestBody TruckVO truckVO);

    /**
     * 根据当前选择的车辆信息获取对应的司机信息
     *
     */

    @PostMapping("/truDriver/selectDriverByplateId")
    public List<DriverVO> getDriverListByPlateId(@RequestBody  DriverVO driverVO);

    /**
     * 获取所有外调车的车队信息（承运商）
     */
    @PostMapping("/truTruckTeam/selectTructList")
    public List<TruckTeamVo> getTruckTeamList(@RequestBody TruckTeamVo truckTeamVo);

    /**
     * 根据选择的车队获取旗下的车辆信息
     */
    @PostMapping("/truTruck/selectPlateNumberByTruckTeamId")
    List<TruckVO> getTruckListByTeamId(@RequestBody  TruckVO truckVO);

    @PostMapping("/truCustomer/selectCustomerForPh")
    List<CustomerVO> getCustomerForPh(@RequestBody  CustomerVO customerVO);

    /**
     * 更改司机状态
     */
    @PostMapping("/truDriver/updateDriverStatus")
    public Boolean updateDriverSta(@RequestBody DriverVO driverVO);
}
