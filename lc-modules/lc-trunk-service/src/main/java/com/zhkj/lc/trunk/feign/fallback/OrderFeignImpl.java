package com.zhkj.lc.trunk.feign.fallback;

import com.zhkj.lc.common.vo.DriverVO;
import com.zhkj.lc.trunk.feign.OrderFeign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description TODO
 * @Author ckj
 * @Date 2019/3/11 17:48
 */
@Service
public class OrderFeignImpl implements OrderFeign {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Integer selectProcOrdByDriverIds(Integer[] driverIds, Integer tenantId) {
        logger.info("根据司机ids和租户id获取正在进行的订单数失败！");
        return null;
    }

    @Override
    public List<DriverVO> selectPlateNumberByProc(Integer tenantId) {
        logger.info("获取在途车辆id失败！");
        return null;
    }
}
