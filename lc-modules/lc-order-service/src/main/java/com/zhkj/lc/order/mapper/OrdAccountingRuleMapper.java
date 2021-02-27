package com.zhkj.lc.order.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zhkj.lc.order.model.entity.OrdAccountingRule;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 计费规则附表 Mapper 接口
 * </p>
 *
 * @author wzc
 * @since 2019-01-11
 */
public interface OrdAccountingRuleMapper extends BaseMapper<OrdAccountingRule> {
    /**
     * 查询计费规则附信息
     *
     * @param id 计费规则附ID
     * @return 计费规则附信息
     */
    OrdAccountingRule selectRuleById(Integer id);

    /**
     * 查询计费规则附列表
     *
     * @param accountingRule 计费规则附信息
     * @return 计费规则附集合
     */
    List<OrdAccountingRule> selectRuleList(OrdAccountingRule accountingRule);

    /**
     * 根据ruleId查询计费规则附列表
     *
     * @param ruleId 计费规则附信息
     * @return 计费规则附集合
     */
    List<OrdAccountingRule> selectRuleListByRuleId(@Param("ruleId") Integer ruleId, @Param("tenantId") Integer tenantId);

    /**
     * 新增计费规则附
     *
     * @param accountingRule 计费规则附信息
     * @return 结果
     */
    int insertAccountingRule(OrdAccountingRule accountingRule);

    /**
     * 修改计费规则附
     *
     * @param accountingRule 计费规则附信息
     * @return 结果
     */
    int updateAccountingRule(OrdAccountingRule accountingRule);

    /**
     * 删除计费规则附
     *
     * @param id 计费规则附ID
     * @return 结果
     */
    int deleteAccountingRuleById(@Param("id") Integer id, @Param("tenantId") Integer tenantId);

    /**
     * 批量删除计费规则附
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteAccountingRuleByIds(String[] ids);
}
