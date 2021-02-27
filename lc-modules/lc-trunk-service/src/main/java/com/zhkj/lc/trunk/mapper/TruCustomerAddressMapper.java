package com.zhkj.lc.trunk.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zhkj.lc.trunk.model.TruCustomer;
import com.zhkj.lc.trunk.model.TruCustomerAddress;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wzc
 * @since 2019-02-11
 */
public interface TruCustomerAddressMapper extends BaseMapper<TruCustomerAddress> {

    List<TruCustomerAddress> selectListByCustomerId(TruCustomerAddress customerId);
}
