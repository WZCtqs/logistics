package com.zhkj.lc.order.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zhkj.lc.order.model.entity.OrderSettlement;

import java.util.List;

/**
 * <p>
 * 订单文件表(派车单、附件、运输拍照) Mapper 接口
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
public interface OrderSettlementMapper extends BaseMapper<OrderSettlement> {

    OrderSettlement selectSettlementByOrderId(String orderId);
}
