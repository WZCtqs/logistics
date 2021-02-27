package com.zhkj.lc.trunk.feign.fallback;

import com.zhkj.lc.trunk.feign.OilCardFeign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @Description 油卡外调服务
 * @Author ckj
 * @Date 2019/3/11 17:11
 */
@Service
public class OilCardFeignImpl implements OilCardFeign {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Integer countOilCard(Integer[] truckIds, Integer tenantId) {
        logger.info("根据司机ids和租户is获取油卡数量失败！");
        return null;
    }

    @Override
    public Integer driversCardNum(Integer driverId, Integer tenantId) {
        return null;
    }
}
