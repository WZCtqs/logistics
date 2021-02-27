package com.zhkj.lc.trunk.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.trunk.mapper.TruDriverMapper;
import com.zhkj.lc.trunk.mapper.TruTruckTeamMapper;
import com.zhkj.lc.trunk.model.TruDriver;
import com.zhkj.lc.trunk.model.TruTruckTeam;
import com.zhkj.lc.trunk.service.ITruTruckTeamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Date;
import java.util.List;

/**
 * 车队 服务层实现
 * 
 * @author yl
 * @date 2018-11-20
 */
@Service
public class TruTruckTeamServiceImpl extends ServiceImpl<TruTruckTeamMapper,TruTruckTeam> implements ITruTruckTeamService {
    @Autowired
    private TruTruckTeamMapper truTruckTeamMapper;
    @Autowired private TruDriverMapper truDriverMapper;

    private static Logger logger = LoggerFactory.getLogger(TruTruckTeamServiceImpl.class);

    @Override
    public Page selectAllTruckTeam(Query query,TruTruckTeam truTruckTeam) {
        List<TruTruckTeam> truTruckTeams = truTruckTeamMapper.selectAllTruckTeam(query,truTruckTeam);
        for(int i = 0;i < truTruckTeams.size(); i++){
            truTruckTeams.get(i).setTruckSum(truTruckTeamMapper.countTruck(truTruckTeams.get(i).getTruckTeamId()));
            truTruckTeams.get(i).setDriverSum(truTruckTeamMapper.countDriver(truTruckTeams.get(i).getTruckTeamId()));
        }
        query.setRecords(truTruckTeams);
        return query;
    }

    @Override
    public boolean deleteByIds(String ids) {
        String[] str = ids.split(",");
// 声明一个int类型的数组.数组长度和String类型的数组长度一致
        int[] truTruckTeamIds = new int[str.length];
// 对String数组进行遍历循环，并转换成int类型的数组
        for (int i = 0; i < str.length; i++) {
            truTruckTeamIds[i] = Integer.parseInt(str[i]);
        }
        TruTruckTeam truTruckTeam = new TruTruckTeam();
        truTruckTeam.setTruckTeamIds(truTruckTeamIds);
        truTruckTeam.setUpdateTime(new Date());
        /*oilApply.setUpdateBy("");*/
        Integer result = truTruckTeamMapper.deleteByIds(truTruckTeam);
        return null != result && result >= 1;
    }

    @Override
    public List<TruTruckTeam> selectByIds(String ids) {
        String[] str = ids.split(",");
// 声明一个int类型的数组.数组长度和String类型的数组长度一致
        int[] truTruckTeamIds = new int[str.length];
// 对String数组进行遍历循环，并转换成int类型的数组
        for (int i = 0; i < str.length; i++) {
            truTruckTeamIds[i] = Integer.parseInt(str[i]);
        }
        TruTruckTeam truTruckTeam = new TruTruckTeam();
        truTruckTeam.setTruckTeamIds(truTruckTeamIds);
        return truTruckTeamMapper.selectByIds(truTruckTeam);
    }

    @Override
    public List<TruTruckTeam> selectAll(TruTruckTeam truTruckTeam) {
        return truTruckTeamMapper.selectAll(truTruckTeam);
    }

    @Override
    public Integer countTruck(Integer truckTeamId){
        return truTruckTeamMapper.countTruck(truckTeamId);
    }

    @Override
    public Integer countDriver(Integer truckTeamId){
        return truTruckTeamMapper.countDriver(truckTeamId);
    }

    @Override
    public boolean setTrustByIds(String ids,String isTrust) {
        String[] str = ids.split(",");
        // 声明一个int类型的数组.数组长度和String类型的数组长度一致
        int[] truTruckTeamIds = new int[str.length];
        // 对String数组进行遍历循环，并转换成int类型的数组
        for (int i = 0; i < str.length; i++) {
            truTruckTeamIds[i] = Integer.parseInt(str[i]);
        }
        TruTruckTeam truTruckTeam = new TruTruckTeam();
        truTruckTeam.setTruckTeamIds(truTruckTeamIds);
        truTruckTeam.setIsTrust(isTrust);
        truTruckTeam.setUpdateTime(new Date());
        /*oilApply.setUpdateBy("");*/
        Integer result = truTruckTeamMapper.setTrustByIds(truTruckTeam);
        return null != result && result >= 1;
    }

    @Override
    public Integer checkTruckTeamName(String teamName, Integer tenantId) {
        return truTruckTeamMapper.checkTruckTeamName(teamName,tenantId);
    }
    @Override
    public Integer checkTruckTeamNameById(String teamName, Integer tenantId) {
        return truTruckTeamMapper.checkTruckTeamNameById(teamName,tenantId);
    }

    @Transactional
    @Override
    public boolean updateById(TruTruckTeam entity) {
        try {
            Integer result = truTruckTeamMapper.updateById(entity);
            return null != result && result == 1;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.info("更新异常信息：" + e.getMessage());
            return false;
         }
    }
}
