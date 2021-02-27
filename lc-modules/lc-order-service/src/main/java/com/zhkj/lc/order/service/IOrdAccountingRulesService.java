package com.zhkj.lc.order.service;

import com.baomidou.mybatisplus.service.IService;
import com.zhkj.lc.common.util.R;
import com.zhkj.lc.order.dto.OrdAccountingRulesDTO;
import com.zhkj.lc.order.model.entity.OrdAccountingRules;

import java.util.List;

/**
 * <p>
 * 计费规则主表 服务类
 * </p>
 *
 * @author wzc
 * @since 2019-01-11
 */
public interface IOrdAccountingRulesService extends IService<OrdAccountingRules> {
    /**
     * 查询计费规则主信息
     *
     * @param id 计费规则主ID
     * @return 计费规则主信息
     */
    OrdAccountingRulesDTO selectAccountingRulesById(Integer id, Integer tenantId);

    /**
     * 查询计费规则主列表
     *
     * @param accountingRules 计费规则主信息
     * @return 计费规则主集合
     */
    List<OrdAccountingRulesDTO> selectAccountingRulesList(OrdAccountingRulesDTO accountingRules);

    List<OrdAccountingRulesDTO> selectRulesListByObjId(OrdAccountingRulesDTO accountingRules);

    /**
     * 删除计费规则主信息
     *
     * @param ruleId 计费规则ID
     * @param num 数量
     * @return 结果
     */
    R<Float> accounting(Integer ruleId, Float kms, Float num, Integer tenantId);
}
