package com.zhkj.lc.trunk.service;


import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.trunk.model.TruCustomer;

import java.util.List;

/**
 * 客户 服务层
 * 
 * @author zhkj
 * @date 2018-11-21
 */
public interface ITruCustomerService extends IService<TruCustomer> {

    public List<TruCustomer> selectAllCustomer(TruCustomer truCustomer);
    public boolean deleteByIds(String ids);

    public Page pageSearch(Query<Object> objectQuery, TruCustomer truCustomer);

    public List<TruCustomer> selectByIds(String ids);

    public List<TruCustomer> selectAll(TruCustomer truCustomer);

    public boolean setTrustByIds(String ids,String isTrust);

    public TruCustomer selectByPhone(TruCustomer truCustomer);

    List<TruCustomer> selectAllCustomerForPh(TruCustomer truCustomer);

    List<TruCustomer> selectAllForFegin(TruCustomer truCustomer);

    List<TruCustomer> selectLikeAllForFegin(TruCustomer truCustomer);

    TruCustomer selectByOpenid(String gopenId);

    TruCustomer selectCustomerById(TruCustomer truCustomer);

    Integer saveCustomer(TruCustomer customer);

    Integer checkCustomerName(String customerName, Integer tenantId);
    Integer checkCustomerNameById(String customerName, Integer tenantId);

    Boolean findByPhone(String phone, Integer tenantId);
    Integer findByPhoneById(String phone, Integer tenantId);

    void insertCustomer(TruCustomer truCustomer);
}
