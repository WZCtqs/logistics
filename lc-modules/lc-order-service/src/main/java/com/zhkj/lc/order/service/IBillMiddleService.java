package com.zhkj.lc.order.service;

import com.baomidou.mybatisplus.service.IService;
import com.zhkj.lc.order.model.entity.BillMiddle;
import com.zhkj.lc.order.model.entity.Expensespayable;

import java.util.List;

/**
 * <p>
 * 应收对账单表 服务类
 * </p>
 *
 * @author wzc
 * @since 2019-02-15
 */
public interface IBillMiddleService extends IService<BillMiddle> {
    List<BillMiddle> selectByAccountPayNumber(BillMiddle billMiddle);

    Boolean removeOrder(BillMiddle billMiddle);

    Boolean removeAllOrder(BillMiddle billMiddle);

    List<BillMiddle> selectOrderIdByaccId(Integer accountId, Integer tenantId);
}
