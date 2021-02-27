package com.zhkj.lc.trunk.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.trunk.model.TruTruckTeam;

import java.util.List;

/**
 * 车队 服务层
 * 
 * @author yl
 * @date 2018-11-20
 */
public interface ITruTruckTeamService extends IService<TruTruckTeam> {

    public Page selectAllTruckTeam(Query<Object> objectQuery, TruTruckTeam truTruckTeam);

    public boolean deleteByIds(String ids);

    public List<TruTruckTeam> selectByIds(String ids);

    public List<TruTruckTeam> selectAll(TruTruckTeam truTruckTeam);

    public Integer countTruck(Integer truckTeamId);

    public Integer countDriver(Integer truckTeamId);

    public boolean setTrustByIds(String ids,String isTrust);

    Integer checkTruckTeamName(String teamName, Integer tenantId);
    Integer checkTruckTeamNameById(String teamName, Integer tenantId);

}
