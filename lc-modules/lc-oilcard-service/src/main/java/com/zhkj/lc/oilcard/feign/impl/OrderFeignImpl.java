package com.zhkj.lc.oilcard.feign.impl;

import com.zhkj.lc.oilcard.feign.OrderFeign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @Description 订单相关接口
 * @Author ckj
 * @Date 2019/2/25 10:45
 */
@Service
public class OrderFeignImpl implements OrderFeign {

    private Logger logger = LoggerFactory.getLogger(OrderFeign.class);

    @Override
    public String selectByDriverIdForOil(Integer driverId, Integer tenantId) {
        logger.info("获取正在进行的订单号失败！");
        return null;
    }

    @Override
    public Integer selectCountByDriver(Integer driverId, Integer tenantId) {
        logger.info("获取司机月订单数失败！");
        return null;
    }

    @Override
    public boolean setSettlementStatus(String orderId) {
        logger.info("setSettlementStatus：feign方法调用失败！");
        return false;
    }

	@Override
	public boolean rechargeCompleted(String orderId, String rechargeBy) {
		logger.info("rechargeCompleted：feign方法调用失败！");
		return false;
	}
}
