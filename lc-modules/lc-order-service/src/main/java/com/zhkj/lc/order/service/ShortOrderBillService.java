package com.zhkj.lc.order.service;

import com.baomidou.mybatisplus.service.IService;
import com.zhkj.lc.common.util.R;
import com.zhkj.lc.order.model.entity.ShortOrderBill;

import java.util.List;

/**
 * @Auther: HP
 * @Date: 2019/7/3 09:18
 * @Description: 
 */
public interface ShortOrderBillService extends IService<ShortOrderBill> {

    public R<Boolean> cerateDZD(String ids, String loginName, Integer tenantId);

    public R<Boolean> deleteBillById(String id, String loginName, Integer tenantId);

    public ShortOrderBill selectOrdersByBillId(String accountBillId, Integer tenantId);

    public R<Boolean> removeShortOrder(String accountBillId, String orderId,String loginName, Integer tenantId);
}
