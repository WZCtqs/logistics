package com.zhkj.lc.trunk.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zhkj.lc.trunk.mapper.TruCustomerAddressMapper;
import com.zhkj.lc.trunk.model.TruCustomer;
import com.zhkj.lc.trunk.model.TruCustomerAddress;
import com.zhkj.lc.trunk.service.ITruCustomerAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wzc
 * @since 2019-02-11
 */
@Service
public class TruCustomerAddressServiceImpl extends ServiceImpl<TruCustomerAddressMapper, TruCustomerAddress> implements ITruCustomerAddressService {

    @Autowired TruCustomerAddressMapper truCustomerAddressMapper;

    @Override
    public List<TruCustomerAddress> selectListByCustomerId(TruCustomerAddress customer) {
        return truCustomerAddressMapper.selectListByCustomerId(customer);
    }
}
