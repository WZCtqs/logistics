package com.zhkj.lc.order.feign.fallback;

import com.zhkj.lc.common.vo.CustomerVO;
import com.zhkj.lc.common.vo.DriverVO;
import com.zhkj.lc.common.vo.TruckTeamVo;
import com.zhkj.lc.common.vo.TruckVO;
import com.zhkj.lc.order.feign.CommonGoodsTruckFeign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class CommonGoodsTruckFeignImpl implements CommonGoodsTruckFeign {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Integer selectTruckIdBy(Integer driverId, String plateNumber) {
        logger.info("调用{}异常","根据司机id和车牌号获取车辆id");
        return null;
    }

    @Override
    public List<TruckVO> getZYTruckList(@RequestBody  TruckVO truckVO) {
        logger.info("调用{}异常","获取自有车");
        return null;
    }

    @Override
    public List<DriverVO> getDriverListByPlateId(@RequestBody  DriverVO driverVO) {
        logger.info("调用{}异常","根据车牌号获取司机信息");
        return null;
    }

    @Override
    public List<TruckTeamVo> getTruckTeamList(@RequestBody  TruckTeamVo truckTeamVo) {
        logger.info("调用{}异常","获取外调车队");
        return null;
    }

    @Override
    public List<TruckVO> getTruckListByTeamId(@RequestBody  TruckVO truckVO) {
        logger.info("调用{}异常","获取车队下的车辆信息");
        return null;
    }

    @Override
    public List<CustomerVO> getCustomerForPh(@RequestBody  CustomerVO customerVO) {
        return null;
    }

    @Override
    public Boolean updateDriverSta(DriverVO driverVO) {
        return null;
    }
}
