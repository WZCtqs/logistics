package com.zhkj.lc.order.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zhkj.lc.order.mapper.OrderSettlementMapper;
import com.zhkj.lc.order.model.entity.OrderSettlement;
import com.zhkj.lc.order.service.IOrdSettlementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auther: HP
 * @Date: 2019/5/25 10:12
 * @Description:
 */
@Service
public class OrdSettlementServiceImpl extends ServiceImpl<OrderSettlementMapper, OrderSettlement> implements IOrdSettlementService {

    @Autowired
    private OrderSettlementMapper settlementMapper;

    @Override
    public OrderSettlement selectSettlementByOrderId(String orderId) {
        return settlementMapper.selectSettlementByOrderId(orderId);
    }
}
