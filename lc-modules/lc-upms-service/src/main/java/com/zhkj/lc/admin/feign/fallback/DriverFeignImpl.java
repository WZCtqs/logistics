package com.zhkj.lc.admin.feign.fallback;

import com.zhkj.lc.admin.feign.DriverFeign;
import com.zhkj.lc.common.vo.CustomerVO;
import com.zhkj.lc.common.vo.DriverVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverFeignImpl implements DriverFeign{

    @Override
    public List<DriverVO> getDriverList() {

        return null;
    }

    @Override
    public Boolean addTanentCustomer(CustomerVO customerVO) {
        return null;
    }

    @Override
    public Boolean insertRate(Integer tenantId) {
        return null;
    }
}
