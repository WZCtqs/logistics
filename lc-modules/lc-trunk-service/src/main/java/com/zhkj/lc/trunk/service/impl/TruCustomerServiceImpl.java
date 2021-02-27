package com.zhkj.lc.trunk.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.trunk.mapper.TruCustomerMapper;
import com.zhkj.lc.trunk.model.TruCustomer;
import com.zhkj.lc.trunk.model.TruTruckTeam;
import com.zhkj.lc.trunk.service.ITruCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 客户 服务层实现
 * 
 * @author zhkj
 * @date 2018-11-21
 */
@Service
public class TruCustomerServiceImpl extends ServiceImpl<TruCustomerMapper,TruCustomer> implements ITruCustomerService{
    @Autowired
    private TruCustomerMapper truCustomerMapper;

    @Override
    public List<TruCustomer> selectAllCustomer(TruCustomer truCustomer) {
        return truCustomerMapper.selectAllCustomer(truCustomer);
    }

    @Override
    public boolean deleteByIds(String ids) {
        String[] str = ids.split(",");
// 声明一个int类型的数组.数组长度和String类型的数组长度一致
        int[] truCustomerIds = new int[str.length];
// 对String数组进行遍历循环，并转换成int类型的数组
        for (int i = 0; i < str.length; i++) {
            truCustomerIds[i] = Integer.parseInt(str[i]);
        }
        TruCustomer truCustomer = new TruCustomer();
        truCustomer.setCustomerIds(truCustomerIds);
        truCustomer.setUpdateTime(new Date());
        /*oilApply.setUpdateBy("");*/
        Integer result = truCustomerMapper.deleteByIds(truCustomer);
        return null != result && result >= 1;
    }

    @Override
    public Page pageSearch(Query query, TruCustomer truCustomer) {
        List<TruCustomer> truCustomers = truCustomerMapper.pageSearch(query,truCustomer);
        query.setRecords(truCustomers);
        return query;
    }

    @Override
    public List<TruCustomer> selectByIds(String ids) {
        String[] str = ids.split(",");
// 声明一个int类型的数组.数组长度和String类型的数组长度一致
        int[] truCustomerIds = new int[str.length];
// 对String数组进行遍历循环，并转换成int类型的数组
        for (int i = 0; i < str.length; i++) {
            truCustomerIds[i] = Integer.parseInt(str[i]);
        }
        TruCustomer truCustomer = new TruCustomer();
        truCustomer.setCustomerIds(truCustomerIds);
        return truCustomerMapper.selectByIds(truCustomer);
    }

    @Override
    public List<TruCustomer> selectAll(TruCustomer truCustomer) {
        return truCustomerMapper.selectAll(truCustomer);
    }

    @Override
    public boolean setTrustByIds(String ids,String isTrust) {
        String[] str = ids.split(",");
        // 声明一个int类型的数组.数组长度和String类型的数组长度一致
        int[] truCustomerIds = new int[str.length];
        // 对String数组进行遍历循环，并转换成int类型的数组
        for (int i = 0; i < str.length; i++) {
            truCustomerIds[i] = Integer.parseInt(str[i]);
        }
        TruCustomer truCustomer = new TruCustomer();
        truCustomer.setCustomerIds(truCustomerIds);
        truCustomer.setIsTrust(isTrust);
        truCustomer.setUpdateTime(new Date());
        /*oilApply.setUpdateBy("");*/
        Integer result = truCustomerMapper.setTrustByIds(truCustomer);
        return null != result && result >= 1;
    }

    @Override
    public TruCustomer selectByPhone(TruCustomer truCustomer){
        return truCustomerMapper.selectByPhone(truCustomer);
    }

    @Override
    public List<TruCustomer> selectAllCustomerForPh(TruCustomer truCustomer) {
        return truCustomerMapper.selectCustomerForPh(truCustomer);
    }

    @Override
    public List<TruCustomer> selectAllForFegin(TruCustomer truCustomer) {
        return truCustomerMapper.selectAllForFegin(truCustomer);
    }

    @Override
    public List<TruCustomer> selectLikeAllForFegin(TruCustomer truCustomer) {
        return truCustomerMapper.selectLikeAllForFegin(truCustomer);
    }

    @Override
    public TruCustomer selectByOpenid(String gopenId){
        return truCustomerMapper.selectByOpenid(gopenId);
    }

    @Override
    public TruCustomer selectCustomerById(TruCustomer truCustomer) {
        return truCustomerMapper.selectCustomerById(truCustomer);
    }

    @Override
    public Integer saveCustomer(TruCustomer customer){
        return truCustomerMapper.saveCustomer(customer);
    }

    @Override
    public Integer checkCustomerName(String customerName, Integer tenantId) {
        return truCustomerMapper.checkCustomerName(customerName,tenantId);
    }
    @Override
    public Integer checkCustomerNameById(String customerName, Integer tenantId) {
        return truCustomerMapper.checkCustomerNameById(customerName,tenantId);
    }

    @Override
    public Boolean findByPhone(String phone, Integer tenantId) {
        Integer i = truCustomerMapper.findByPhone(phone, tenantId);
        if(i!=0){
            return false;//手机号不重复
        }
        return true;
    }

    @Override
    public Integer findByPhoneById(String phone, Integer tenantId) {
        return truCustomerMapper.findByPhoneById(phone, tenantId);
    }

    @Override
    public void insertCustomer(TruCustomer truCustomer) {
        truCustomerMapper.insertCustomer(truCustomer);
    }
}
