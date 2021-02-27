package com.zhkj.lc.order.controller;
import java.util.List;
import java.util.Map;
import java.util.Date;

import com.zhkj.lc.common.util.UserUtils;
import com.zhkj.lc.order.model.entity.OrdAccountingRule;
import com.zhkj.lc.order.service.IOrdAccountingRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.zhkj.lc.common.util.R;
import com.zhkj.lc.common.web.BaseController;

/**
 * <p>
 * 计费规则附表 前端控制器
 * </p>
 *
 * @author wzc
 * @since 2019-01-11
 */
@RestController
@RequestMapping("/ordAccountingRule")
public class OrdAccountingRuleController extends BaseController {
    @Autowired private IOrdAccountingRuleService ordAccountingRuleService;

    /**
    * 通过ID查询
    *
    * @param id ID
    * @return OrdAccountingRule
    */
    @GetMapping("/{id}")
    public R<OrdAccountingRule> get(@PathVariable Integer id) {
        return new R<>(ordAccountingRuleService.selectById(id));
    }

    /**
    * 根据主表id查询信息
    *
    * @param ruleId 分页对象
    * @return 分页对象
    */
    @RequestMapping("/getList")
    public List<OrdAccountingRule> getListByRuleId(Integer ruleId) {
        return ordAccountingRuleService.selectRuleListByRuleId(ruleId, getTenantId());
    }

    /**
     * 添加
     * @param  ordAccountingRule  实体
     * @return success/false
     */
    @PostMapping
    public R<Boolean> add(@RequestBody OrdAccountingRule ordAccountingRule) {
        ordAccountingRule.setCreateBy(UserUtils.getUser());
        ordAccountingRule.setCreateTime(new Date());
        ordAccountingRule.setTenantId(getTenantId());
        return new R<>(ordAccountingRuleService.insert(ordAccountingRule));
    }

    /**
     * 删除
     * @param id ID
     * @return success/false
     */
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable Integer id) {
        return new R<>(ordAccountingRuleService.deleteById(id));
    }

    /**
     * 编辑
     * @param  ordAccountingRule  实体
     * @return success/false
     */
    @PutMapping
    public R<Boolean> edit(@RequestBody OrdAccountingRule ordAccountingRule) {
        ordAccountingRule.setUpdateTime(new Date());
        return new R<>(ordAccountingRuleService.updateById(ordAccountingRule));
    }
}
