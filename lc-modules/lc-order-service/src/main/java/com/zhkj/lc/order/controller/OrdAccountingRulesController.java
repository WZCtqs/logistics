package com.zhkj.lc.order.controller;
import java.util.List;
import java.util.Map;
import java.util.Date;

import com.zhkj.lc.common.util.UserUtils;
import com.zhkj.lc.order.dto.OrdAccountingRulesDTO;
import com.zhkj.lc.order.model.entity.OrdAccountingRule;
import com.zhkj.lc.order.model.entity.OrdAccountingRules;
import com.zhkj.lc.order.service.IOrdAccountingRuleService;
import com.zhkj.lc.order.service.IOrdAccountingRulesService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import com.zhkj.lc.common.constant.CommonConstant;
import com.zhkj.lc.common.util.R;
import com.zhkj.lc.common.web.BaseController;

/**
 * <p>
 * 计费规则主表 前端控制
 * </p>
 *
 * @author wzc
 * @since 2019-01-11
 */
@RestController
@RequestMapping("/ordAccountingRules")
public class OrdAccountingRulesController extends BaseController {
    @Autowired private IOrdAccountingRulesService ordAccountingRulesService;
    @Autowired private IOrdAccountingRuleService ordAccountingRuleService;

    /**
    * 通过ID查询
    *
    * @param id ID
    * @return OrdAccountingRules
    */
    @GetMapping("/{id}")
    public OrdAccountingRules get(@PathVariable Integer id) {
        return ordAccountingRulesService.selectAccountingRulesById(id, getTenantId());
    }


    /**
    * 查询计费规则list
    *
    * @param ordAccountingRules
    * @return
    */
    @RequestMapping("/getList")
    public List<OrdAccountingRulesDTO> getRulesList(OrdAccountingRulesDTO ordAccountingRules) {
        ordAccountingRules.setTenantId(getTenantId());
        return ordAccountingRulesService.selectAccountingRulesList(ordAccountingRules);
    }

    /**
     * 查询计费规则list
     *
     * @param ordAccountingRules 计费规则list
     * @return
     */
    @ApiOperation(value = "根据司机id或者客户id查询，司机id或者客户id必填，计费规则类型：发货方、承运商必填。")
    @RequestMapping("/getListByObjId")
    public List<OrdAccountingRulesDTO> getRulesListByObjId(OrdAccountingRulesDTO ordAccountingRules) {
        ordAccountingRules.setTenantId(getTenantId());
        return ordAccountingRulesService.selectRulesListByObjId(ordAccountingRules);
    }

    /**
     * 添加
     * @param  ordAccountingRules  实体
     * @return success/false
     */
    @PostMapping
    public R<Boolean> add(@RequestBody OrdAccountingRules ordAccountingRules) {
        ordAccountingRules.setCreateBy(UserUtils.getUser());
        ordAccountingRules.setCreateTime(new Date());
        ordAccountingRules.setDelFlag(CommonConstant.STATUS_NORMAL);
        ordAccountingRules.setTenantId(getTenantId());
        return new R<>(ordAccountingRulesService.insert(ordAccountingRules));
    }

    /**
     * 删除
     * @param id ID
     * @return success/false
     */
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable Integer id) {
        OrdAccountingRules ordAccountingRules = new OrdAccountingRules();
        ordAccountingRules.setId(id);
        ordAccountingRules.setUpdateTime(new Date());
        ordAccountingRules.setDelFlag(CommonConstant.STATUS_DEL);
        return new R<>(ordAccountingRulesService.updateById(ordAccountingRules));
    }

    /**
     * 编辑
     * @param  ordAccountingRules  实体
     * @return success/false
     */
    @PutMapping
    @Transactional
    public R<Boolean> edit(@RequestBody OrdAccountingRulesDTO ordAccountingRules) {
        ordAccountingRules.setUpdateTime(new Date());
        ordAccountingRules.setUpdateBy(UserUtils.getUser());
        OrdAccountingRules accountingRules = new OrdAccountingRules();
        accountingRules.setId(ordAccountingRules.getId());
        accountingRules.setRuleName(ordAccountingRules.getRuleName()==null?"":ordAccountingRules.getRuleName());
        accountingRules.setRuleType(ordAccountingRules.getRuleType()==null?"":ordAccountingRules.getRuleType());
        accountingRules.setStartNum(ordAccountingRules.getStartNum()==null?null:ordAccountingRules.getStartNum());
        accountingRules.setUpdateBy(UserUtils.getUser());
        accountingRules.setUpdateTime(new Date());
        /*更新主表*/
        ordAccountingRulesService.updateById(accountingRules);
        /*插入附表*/
        List<OrdAccountingRule> rules = ordAccountingRules.getOrdAccountingRule();
        if(rules != null && rules.size()>0){
            /*删除附表*/
            ordAccountingRuleService.deleteAccountingRuleById(ordAccountingRules.getId(),getTenantId());
            for(OrdAccountingRule rule : rules){
                rule.setCreateBy(UserUtils.getUser());
                rule.setCreateTime(new Date());
                rule.setRuleId(ordAccountingRules.getId());
                rule.setTenantId(getTenantId());
                ordAccountingRuleService.insert(rule);
            }
        }
        return new R<>(Boolean.TRUE);
    }

    /**
     *
     * 功能描述: 计费规则计算费用
     *
     * @param ruleId	规则id
     * @param kms	公里数
     * @param num	货品量
     * @return java.lang.Float
     * @auther wzc
     * @date 2019/1/12 16:38
     */
    @GetMapping("accountingByRule")
    public R<Float> accounting(Integer ruleId,Float kms, Float num){
        return ordAccountingRulesService.accounting(ruleId, kms, num, getTenantId());
    }
}
