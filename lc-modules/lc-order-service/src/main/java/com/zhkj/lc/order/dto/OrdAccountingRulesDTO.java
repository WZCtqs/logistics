package com.zhkj.lc.order.dto;

import com.zhkj.lc.order.model.entity.OrdAccountingRule;
import com.zhkj.lc.order.model.entity.OrdAccountingRules;
import lombok.Data;

import java.util.List;

/**
 * @Auther: HP
 * @Date: 2019/1/15 15:23
 * @Description:
 */
@Data
public class OrdAccountingRulesDTO extends OrdAccountingRules {

    /*
     * 对象名称
     */
    private String objName;
    /**
     * 规则对象idss
     */
    private Integer[] ruleObjIds;

    private List<OrdAccountingRule> ordAccountingRule;
}
