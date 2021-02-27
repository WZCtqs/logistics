package com.zhkj.lc.order.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zhkj.lc.order.mapper.OrdAccountingRuleMapper;
import com.zhkj.lc.order.model.entity.OrdAccountingRule;
import com.zhkj.lc.order.service.IOrdAccountingRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 计费规则附表 服务实现类
 * </p>
 *
 * @author wzc
 * @since 2019-01-11
 */
@Service
public class OrdAccountingRuleServiceImpl extends ServiceImpl<OrdAccountingRuleMapper, OrdAccountingRule> implements IOrdAccountingRuleService {

    @Autowired private OrdAccountingRuleMapper ruleMapper;

    @Override
    public List<OrdAccountingRule> selectRuleListByRuleId(Integer ruleId, Integer tenantId) {
        return ruleMapper.selectRuleListByRuleId(ruleId, tenantId);
    }

    @Override
    public int deleteAccountingRuleById(Integer id, Integer tenantId) {
        return ruleMapper.deleteAccountingRuleById(id, tenantId);
    }
}
