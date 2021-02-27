package com.zhkj.lc.order.service;

import com.baomidou.mybatisplus.service.IService;
import com.zhkj.lc.order.model.entity.OrdAccountingRule;

import java.util.List;

/**
 * <p>
 * 计费规则附表 服务类
 * </p>
 *
 * @author wzc
 * @since 2019-01-11
 */
public interface IOrdAccountingRuleService extends IService<OrdAccountingRule> {

    /**
     * 查询计费规则附列表
     *
     * @param ruleId 计费规则id
     * @return 计费规则附集合
     */
    List<OrdAccountingRule> selectRuleListByRuleId(Integer ruleId, Integer tenantId);

    /**
     * 删除计费规则附信息
     *
     * @param id 需要删除的数据ID
     * @return 结果
     */
    int deleteAccountingRuleById(Integer id, Integer tenantId);
}
