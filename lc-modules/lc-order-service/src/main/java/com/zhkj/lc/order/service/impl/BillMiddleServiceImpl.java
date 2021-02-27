package com.zhkj.lc.order.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zhkj.lc.order.mapper.BillMiddleMapper;
import com.zhkj.lc.order.mapper.ExpensespayableMapper;
import com.zhkj.lc.order.model.entity.BillMiddle;
import com.zhkj.lc.order.model.entity.Expensespayable;
import com.zhkj.lc.order.service.IBillMiddleService;
import com.zhkj.lc.order.service.IExpensespayableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 应收对账单表 服务实现类
 * </p>
 *
 * @author wzc
 * @since 2019-02-15
 */
@Service
public class BillMiddleServiceImpl extends ServiceImpl<BillMiddleMapper, BillMiddle> implements IBillMiddleService {
@Autowired BillMiddleMapper billMiddleMapper;

    @Override
    public List<BillMiddle> selectByAccountPayNumber(BillMiddle billMiddle) {
        return billMiddleMapper.selectByAccountPayNumber(billMiddle);
    }

    @Override
    @Transactional
    public Boolean removeOrder(BillMiddle billMiddle) {
        return billMiddleMapper.removeOrder(billMiddle);
    }

    @Override
    public Boolean removeAllOrder(BillMiddle billMiddle) {
        return billMiddleMapper.removeAllOrder(billMiddle);
    }

    @Override
    public List<BillMiddle> selectOrderIdByaccId(Integer accountId, Integer tenantId) {
        return billMiddleMapper.selectOrderIdByaccId(accountId, tenantId);
    }
}
