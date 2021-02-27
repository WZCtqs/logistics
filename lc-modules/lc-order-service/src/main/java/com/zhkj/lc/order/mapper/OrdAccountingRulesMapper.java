package com.zhkj.lc.order.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zhkj.lc.order.dto.OrdAccountingRulesDTO;
import com.zhkj.lc.order.model.entity.OrdAccountingRules;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 计费规则主表 Mapper 接口
 * </p>
 *
 * @author wzc
 * @since 2019-01-11
 */
public interface OrdAccountingRulesMapper extends BaseMapper<OrdAccountingRules> {
    /**
     * 查询计费规则主信息
     *
     * @param id 计费规则主ID
     * @return 计费规则主信息
     */
    OrdAccountingRulesDTO selectAccountingRulesById(@Param("id") Integer id, @Param("tenantId") Integer tenantId);

    /**
     * 查询计费规则主列表
     *
     * @param accountingRules 计费规则主信息
     * @return 计费规则主集合
     */
    List<OrdAccountingRulesDTO> selectRulesList(OrdAccountingRulesDTO accountingRules);
}
