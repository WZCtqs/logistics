package com.zhkj.lc.admin.feign;

import com.zhkj.lc.admin.feign.fallback.DriverFeignImpl;
import com.zhkj.lc.common.util.R;
import com.zhkj.lc.common.vo.CustomerVO;
import com.zhkj.lc.common.vo.DriverVO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
/*@FeignClient(name="lc-truck-service",url="${dlc.trunk.ip}")*/
@FeignClient(value = "lc-truck-service" ,fallback = DriverFeignImpl.class)
public interface DriverFeign {

    /*查询司机列表*/
    @GetMapping("truDriver/selectDriverPlateNumber")
    List<DriverVO> getDriverList();

    /**
     * 新增客户信息
     */

    @PostMapping("/truCustomer/addCustomerForTenant")
    Boolean addTanentCustomer(@RequestBody CustomerVO customerVO);

    @PostMapping("/loanRate/insert")
    Boolean insertRate(@RequestBody Integer tenantId);
}
