package com.zhkj.lc.oilcard.feign.impl;

import com.zhkj.lc.common.util.R;
import com.zhkj.lc.common.vo.AnnouncementVO;
import com.zhkj.lc.common.vo.DriverVO;
import com.zhkj.lc.common.vo.TruckVO;
import com.zhkj.lc.oilcard.feign.TruckFeign;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TruckFeignImpl implements TruckFeign {

//    private Logger logger = LoggerFactory.getLogger(TruckVO.class);

    @Override
    public TruckVO getTruckByid(Integer id) {
//        logger.info("根据车辆id获取车辆信息失败！");
        return null;
    }

    @Override
    public List<TruckVO> listTruck(TruckVO truckVO) {
//        logger.info("获取全部车牌号失败！");
        return null;
    }

    @Override
    public DriverVO getDriverByid(Integer id) {
//        logger.info("根id获取司机信息失败！");
        return null;
    }

    @Override
    public R<Boolean> edit(DriverVO driverVO) {
        return null;
    }

    @Override
    public List<DriverVO> selectDriverByplateId(DriverVO driverVO) {
//        logger.info("根车辆id获取司机信息失败！");
        return null;
    }

    @Override
    public List<DriverVO> selectAllDrivers(DriverVO driverVO) {
//        logger.info("获取全部司机信息失败！");
        return null;
    }

    @Override
    public String getDriverPayType(Integer driverId, Integer tenantId) {
//        logger.info("获取司机所属车队结算方式失败！");
        return null;
    }

    @Override
    public R<Boolean> addFeign(AnnouncementVO announcementVO) {
//        logger.info("添加公告信息失败！");
        return null;
    }
}
