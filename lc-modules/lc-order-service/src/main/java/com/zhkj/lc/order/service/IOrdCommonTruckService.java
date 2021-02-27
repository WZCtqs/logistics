package com.zhkj.lc.order.service;

import com.zhkj.lc.order.dto.OrdCommonTruckVO;
import com.zhkj.lc.order.model.entity.OrdCommonTruck;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author cb
 * @since 2019-01-03
 */
public interface IOrdCommonTruckService extends IService<OrdCommonTruck> {
    OrdCommonTruckVO selectCommonTruck(String orderId);
}
