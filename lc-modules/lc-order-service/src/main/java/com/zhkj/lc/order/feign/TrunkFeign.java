package com.zhkj.lc.order.feign;

import com.zhkj.lc.common.util.R;
import com.zhkj.lc.common.vo.*;
import com.zhkj.lc.order.dto.TruDriver;
import com.zhkj.lc.order.feign.fallback.TrunkFeignImpl;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@FeignClient(value = "lc-truck-service" ,fallback = TrunkFeignImpl.class)
public interface TrunkFeign {

    @GetMapping("truDriver/checkDriverTruck")
    Integer checkDriverTruck(@RequestParam("driverName") String driverName, @RequestParam("plateNumber") String plateNumber, @RequestParam("tenantId") Integer tenantId);

    @GetMapping(value="/truDriver/selectDriverById")
    DriverVO getDriverByid(@RequestParam("id") Integer id);

    /*查询司机列表*/
    @PostMapping("/truDriver/selectDriverByplateId")
    List<DriverVO> getDriverList(@RequestBody DriverVO driverVO);

    /*根据车辆信息查询list*/
    @PostMapping("/truTruck/selectPlateNumberByAttribute")
    List<TruckVO> selectTruckList(@RequestBody TruckVO truckVO);

    /**
     * 通过姓名查询司机信息
     * @param driverName
     * @return
     */
    @GetMapping("/truDriver/selectDriverByName/{driverName}/{tenantId}")
    TruDriver getDriverByName(@PathVariable("driverName") String driverName,@PathVariable("tenantId") Integer tenantId);

    /*根据车辆id查询*/
    @GetMapping("/truTruck/selectTruckByPlateNumber/{plateNumber}/{tenantId}")
    TruckVO selectTruckByPlateNumber(@RequestParam("plateNumber") String plateNumber, @RequestParam("tenantId") Integer tenantId);

    @GetMapping("/truDriver/getDriverTeamType")
    List<DriverVO> getDriverTeamType(@RequestParam("teamType") String teamType, @RequestParam("tenantId") Integer tenantId);

    /*查询所有客户*/
    @PostMapping("truCustomer/selectCustomerForPh")
    List<CustomerVO> getCustomerList(@RequestBody CustomerVO customerVO);

    /*根据客户id查找客户信息*/
    @PostMapping("truCustomer/selectCustomerById")
    CustomerVO selectCustomerById(@RequestBody CustomerVO customerVO);

    /*根据id查询车队信息*/
    @PostMapping("/truTruckTeam/selectTructList")
    List<TruckTeamVo> getTruckTeams(@RequestBody TruckTeamVo truckTeamVo);

    /*根据id查询车队信息*/
    @GetMapping("truDriver/getZFDriver/{zfId}")
    List<DriverVO> getZFDriverInfo(@PathVariable("zfId") String zfId);

    @PutMapping("truDriver")
    R<Boolean> edit(@RequestBody DriverVO truDriver);

    /*添加公告通知*/
    @PostMapping("/announcement/add/feign")
    R<Boolean> add(@RequestBody AnnouncementVO announcementVO);

    /**
     * 后台：查询所有未查看的公告基础信息
     *
     * @return 公告集合
     */
    @GetMapping("/announcement/listAllNoChecked")
    List<AnnouncementVO> listAllNoChecked(@RequestParam("tenantId") Integer tenantId);

    /**
     * 将客服公告通知转为已读
     *
     * @param ids 公告ids
     * @return 结果
     */
    @GetMapping("/announcement/2Checked/{ids}")
    R<Boolean> update2CheckedById(@PathVariable("ids") String ids);

    /*查询所有客户*/
    @PostMapping("truCustomer/selectAllCustomers")
    List<CustomerVO> selectAllCustomers(@RequestBody CustomerVO customerVO);

    @PostMapping("truTruckFee/sumOtherFeeLast7days")
    TruckTeamFeeVO selectTruckFeeByDriverLast7days(FeeVO feeVO);

    @PostMapping("truTruckFee/sumOtherFeeMonthdays")
    TruckTeamFeeVO selectTruckFeeByDriverMonthdays(FeeVO feeVO);

    @PostMapping("truTruckFee/sumOtherFeeCurrentSeason")
    TruckTeamFeeVO selectTruckFeeByDriverCurrentSeason(FeeVO feeVO);

    @PostMapping("truTruckFee/sumOtherFeeLast6Months")
    TruckTeamFeeVO selectTruckFeeByDriverLast6Months(FeeVO feeVO);

    @PostMapping("truTruckFee/sumOtherFeeSometime")
    TruckTeamFeeVO selectTruckFeeByDriverSometime(FeeVO feeVO);

    @GetMapping("truDriver/selectDriverByPlateNumber")
    List<DriverVO> selectDriverByPlateNumber(@RequestParam("plateId") Integer plateId,@RequestParam("tenantId") Integer tenantId);

    @GetMapping("truDriver/selectDriverByDriverId")
    DriverVO selectDriverByDriverId(@RequestParam("driverId")Integer driverId,@RequestParam("tenantId")Integer tenantId);

    @PostMapping("truDriver/selectAllDriverByTenantId")
    List<DriverVO>selectAllDriver(@RequestBody DriverVO driverVO);

    /*查询所有客户*/
    @PostMapping("truCustomer/selectAllForFegin")
    List<CustomerVO> selectAllForFegin(@RequestBody CustomerVO customerVO);
    /*查询所有客户(模糊查询)*/
    @PostMapping("truCustomer/selectLikeAllForFegin")
    List<CustomerVO> selectLikeAllForFegin(@RequestBody CustomerVO customerVO);

    /*查询车辆下在途司机个数*/
    @GetMapping("truTruck/checkTruckDriverStatus")
    Integer checkTruckDriverStatus(@RequestParam("plateNumber")String plateNumber,@RequestParam("tenantId")Integer tenantId);

    @GetMapping("truDriver/selectDriverStatus")
    DriverVO selectDriverStatus(@RequestParam("driverId")Integer driverId,@RequestParam("tenantId")Integer tenantId);

    @GetMapping("truDriver/selectDriverStatusByPlateNumber/{plateNumber}")
    List<DriverVO> selectDriverStatusByPlateNumber(@PathVariable("plateNumber") String plateNumber);

    @GetMapping("taxRate/{tenantId}")
    TaxRateVO getRate(@PathVariable("tenantId") Integer tenantId);

    /*查询车主*/
    @PostMapping("/truTruckOwn/getAllTruTruckOwn")
    List<TruTruckOwnVo> getAllTruTruckOwn(@RequestBody TruTruckOwnVo truTruckOwnVo);

    /*根据车主id查询车牌号*/
    @PostMapping("/truTruckOwn/getPlateNumbersByTruckOwnId")
    List<String> getPlateNumbersByTruckOwnId(@RequestParam("truckOwnId") Integer truckOwnId,@RequestParam("tenantId") Integer tenantId);

    /*根据车牌号查询车主*/
    @PostMapping("/truTruckOwn/selectOwnerByPlateNumber")
    TruTruckOwnVo getTruTruckOwnVoByPlateNumber(@RequestParam("plateNumber") String plateNumber,@RequestParam("tenantId") Integer tenantId);
}
