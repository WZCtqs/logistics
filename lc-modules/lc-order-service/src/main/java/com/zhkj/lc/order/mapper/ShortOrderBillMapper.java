package com.zhkj.lc.order.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zhkj.lc.order.model.entity.ShortOrderBill;

/**
 * @Auther: HP
 * @Date: 2019/7/3 09:18
 * @Description: 
 */
public interface ShortOrderBillMapper extends BaseMapper<ShortOrderBill> {

    ShortOrderBill selectByAccountBillId(String accountBillId);

    boolean updateByPrimaryKey(ShortOrderBill bill);

}