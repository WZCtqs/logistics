package com.zhkj.lc.trunk.service;


import com.baomidou.mybatisplus.service.IService;
import com.zhkj.lc.trunk.model.TruCustomer;
import com.zhkj.lc.trunk.model.TruCustomerAddress;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wzc
 * @since 2019-02-11
 */
public interface ITruCustomerAddressService extends IService<TruCustomerAddress> {

    List<TruCustomerAddress> selectListByCustomerId(TruCustomerAddress customer);
}
