package com.zhkj.lc.order.service;

import com.baomidou.mybatisplus.service.IService;
import com.zhkj.lc.order.model.entity.OrderSettlement;


/**
 * @Auther: HP
 * @Date: 2019/5/25 10:11
 * @Description:
 */
public interface IOrdSettlementService extends IService<OrderSettlement> {

    OrderSettlement selectSettlementByOrderId(String orderId);

}
