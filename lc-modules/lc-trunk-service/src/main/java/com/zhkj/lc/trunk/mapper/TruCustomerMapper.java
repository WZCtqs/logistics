package com.zhkj.lc.trunk.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.trunk.model.TruCustomer;
import com.zhkj.lc.trunk.model.TruTruckTeam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 客户 数据层
 * 
 * @author zhkj
 * @date 2018-11-21
 */
@Repository
public interface TruCustomerMapper extends BaseMapper<TruCustomer>
{

    public List<TruCustomer> selectAllCustomer(TruCustomer truCustomer);

    public Integer deleteByIds(TruCustomer truCustomer);

    public List<TruCustomer> pageSearch(Query<Object> objectQuery,TruCustomer truCustomer);

    public TruCustomer selectCustomerById(TruCustomer truCustomer);

    public TruCustomer selectCustomerByCustomerId(@Param("customerId") int customerId);

    public List<TruCustomer> selectByIds(TruCustomer truCustomer);

    public List<TruCustomer> selectAll(TruCustomer truCustomer);

    public Integer setTrustByIds(TruCustomer truCustomer);

    public TruCustomer selectByPhone(TruCustomer truCustomer);

    List<TruCustomer> selectCustomerForPh(TruCustomer truCustomer);

    List<TruCustomer> selectAllForFegin(TruCustomer truCustomer);

    List<TruCustomer> selectLikeAllForFegin(TruCustomer truCustomer);

    TruCustomer selectByOpenid(@RequestParam("gopenId") String gopenId);

    Integer saveCustomer(TruCustomer customer);

    Integer checkCustomerName(@Param("customerName")String customerName, @Param("tenantId")Integer tenantId);
    Integer checkCustomerNameById(@Param("customerName")String customerName, @Param("tenantId")Integer tenantId);

    Integer findByPhone(@Param("phone")String phone, @Param("tenantId")Integer tenantId);
    Integer findByPhoneById(@Param("phone")String phone, @Param("tenantId")Integer tenantId);

    void insertCustomer(TruCustomer truCustomer);
}