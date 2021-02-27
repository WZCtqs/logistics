package com.zhkj.lc.order.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zhkj.lc.common.util.R;
import com.zhkj.lc.common.vo.CustomerVO;
import com.zhkj.lc.common.vo.DriverVO;
import com.zhkj.lc.common.vo.TruckTeamVo;
import com.zhkj.lc.order.dto.OrdAccountingRulesDTO;
import com.zhkj.lc.order.feign.TrunkFeign;
import com.zhkj.lc.order.mapper.OrdAccountingRulesMapper;
import com.zhkj.lc.order.model.entity.OrdAccountingRule;
import com.zhkj.lc.order.model.entity.OrdAccountingRules;
import com.zhkj.lc.order.service.IOrdAccountingRulesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 计费规则主表 服务实现类
 * </p>
 *
 * @author wzc
 * @since 2019-01-11
 */
@Service
public class OrdAccountingRulesServiceImpl extends ServiceImpl<OrdAccountingRulesMapper, OrdAccountingRules>
        implements IOrdAccountingRulesService {

    @Autowired OrdAccountingRulesMapper rulesMapper;

    @Autowired TrunkFeign trunkFeign;

    /**
     * 通过ID查询
     *
     * @param id ID
     * @return OrdAccountingRules
     */
    @Override
    public OrdAccountingRulesDTO selectAccountingRulesById(Integer id, Integer tenantId) {
        OrdAccountingRulesDTO rulesDTO = rulesMapper.selectAccountingRulesById(id, tenantId);
        if(rulesDTO.getOrdAccountingRule().size()==0){
            OrdAccountingRule rule = new OrdAccountingRule();
            List<OrdAccountingRule> rules = rulesDTO.getOrdAccountingRule();
            rules.add(rule);
            rulesDTO.setOrdAccountingRule(rules);
        }
        return rulesDTO;
    }

    /**
     *
     * 功能描述: 查询计费规则列表
     *
     * @param ordRule
     * @return java.util.List<com.zhkj.lc.order.model.entity.OrdAccountingRules>
     * @auther wzc
     * @date 2019/1/14 14:18
     */
    @Override
    public List<OrdAccountingRulesDTO> selectAccountingRulesList(OrdAccountingRulesDTO ordRule) {
        Integer tenantId = ordRule.getTenantId();
        switch (ordRule.getObjType()){
            case "发货方":
                ArrayList<Integer> customers = getCustomers(ordRule.getObjName());
                Integer[] objFHIds = new Integer[customers.size()];
                customers.toArray(objFHIds);
                ordRule.setRuleObjIds(objFHIds);
                break;
            case "承运商":
                ArrayList<Integer> truckTeamIds = getTruckTeamIds(ordRule.getObjName());
                Integer[] objCDIds = new Integer[truckTeamIds.size()];
                truckTeamIds.toArray(objCDIds);
                ordRule.setRuleObjIds(objCDIds);
                break;
        }
        List<OrdAccountingRulesDTO> rules = rulesMapper.selectRulesList(ordRule);
        rules.forEach(rule->{
            String objName = getObjName(rule.getRuleObjId(), rule.getObjType(),tenantId);
            rule.setObjName(objName);
        });
        return rules;
    }

    @Override
    public List<OrdAccountingRulesDTO> selectRulesListByObjId(OrdAccountingRulesDTO ordRule) {
        Integer [] objs = new Integer[1];
        if(ordRule.getObjType().equals("承运商")){
            DriverVO driverVO = new DriverVO();
            driverVO.setDriverId(ordRule.getRuleObjId());
            driverVO.setTenantId(ordRule.getTenantId());
            List<DriverVO>driverVOS = trunkFeign.getDriverList(driverVO);
            if(driverVOS !=null && driverVOS.size() == 1){
                objs[0] = driverVOS.get(0).getTruckTeamId();
            }
        }else {
            objs[0] = ordRule.getRuleObjId();
        }
        ordRule.setRuleObjIds(objs);
        return rulesMapper.selectRulesList(ordRule);
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
    @Override
    public R<Float> accounting(Integer ruleId, Float kms, Float num, Integer tenantId) {
        BigDecimal money = null;
        OrdAccountingRulesDTO rules = rulesMapper.selectAccountingRulesById(ruleId, tenantId);
        if(rules != null){
            List<OrdAccountingRule> ruleList = rules.getOrdAccountingRule();
            if(ruleList == null || ruleList.size() == 0) {
                return new R<>(null, "请先设置计费规则！");
            }
            num = num > rules.getStartNum()?num:rules.getStartNum();
            int s = 0;
            for(OrdAccountingRule r:ruleList){
                s++;
                if(num <= r.getLimitNum()){
                    BigDecimal bnum = new BigDecimal(num);
                    BigDecimal bkms = new BigDecimal(kms);
                    money = bkms.multiply(bnum).multiply(r.getPrice());
                    break;
                }
            }
            if(s == ruleList.size()){
                return new R<>(null,"没有该数量等级的计费规则，请先设置计费规则！");
            }
        }
        if(money==null){
            return new R<>(0f,"费用计算失败，请稍后重试！");
        }
        return new R<>(money.floatValue());
    }
    /**
     *
     * 功能描述: 获取规则对象的名称
     *
     * @param objId	对象id
     * @param objType	对象类型
     * @return java.lang.String
     * @auther wzc
     * @date 2019/1/14 14:15
     */
    public String getObjName(Integer objId, String objType, Integer tenantId){
        String objName = null;
        switch (objType){
            case "发货方":
                CustomerVO customerVO = new CustomerVO();
                customerVO.setTenantId(tenantId);
                customerVO.setCustomerId(objId);
                List<CustomerVO> customers = trunkFeign.selectAllForFegin(customerVO);
                if(customers != null && customers.size() == 1){
                    objName = customers.get(0).getCustomerName();
                }else {
                    objName = null;
                }
                break;
            case "承运商":
                TruckTeamVo teamVo = new TruckTeamVo();
                teamVo.setTenantId(tenantId);
                teamVo.setTruckTeamId(objId);
                List<TruckTeamVo> teams = trunkFeign.getTruckTeams(teamVo);
                if(teams != null && teams.size() == 1){
                    objName = teams.get(0).getTeamName();
                }else {
                    objName = null;
                }
                break;
        }
        return objName;
    }

    /**
     *
     * 功能描述: 根据车队名称模糊查询出相似的车队
     *
     * @param objName
     * @return 车队idList
     * @auther wzc
     * @date 2019/1/14 14:16
     */
    public ArrayList<Integer>getTruckTeamIds(String objName){
        ArrayList<Integer> truckTeamIds = new ArrayList();
        if(objName!=null && objName != ""){
            TruckTeamVo teamVo = new TruckTeamVo();
            teamVo.setTeamName(objName);
            List<TruckTeamVo>teamVos = trunkFeign.getTruckTeams(teamVo);
            for(TruckTeamVo tm : teamVos){
                truckTeamIds.add(tm.getTruckTeamId());
            }
        }
        return truckTeamIds;
    }

    public ArrayList<Integer>getCustomers(String customerName){
        ArrayList<Integer> customers = new ArrayList();
        if(customerName!=null && customerName != ""){
            CustomerVO customerVO = new CustomerVO();
            customerVO.setCustomerName(customerName);
            List<CustomerVO>customerVOS = trunkFeign.selectLikeAllForFegin(customerVO);
            for(CustomerVO c : customerVOS){
                customers.add(c.getCustomerId());
            }
        }
        return customers;
    }
}
