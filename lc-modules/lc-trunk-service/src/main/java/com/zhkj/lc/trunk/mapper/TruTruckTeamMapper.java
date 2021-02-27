package com.zhkj.lc.trunk.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.trunk.model.TruTruckTeam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 车队 数据层
 * 
 * @author yl
 * @date 2018-11-20
 */
public interface TruTruckTeamMapper extends BaseMapper<TruTruckTeam>
{

    public List<TruTruckTeam> selectAllTruckTeam(Query<Object> query, TruTruckTeam truTruckTeam);

    public Integer deleteByIds(TruTruckTeam truTruckTeam);

    public TruTruckTeam selectTruckTeamById(@Param("truckTeamId") Integer truckTeamId);

    public List<TruTruckTeam> selectByIds(TruTruckTeam truTruckTeam);

    public List<TruTruckTeam> selectAll(TruTruckTeam truTruckTeam);

    public Integer countTruck(Integer truckTeamId);

    public Integer countDriver(Integer truckTeamId);

    public Integer setTrustByIds(TruTruckTeam truTruckTeam);

    Integer checkTruckTeamName(@Param("teamName")String teamName, @Param("tenantId")Integer tenantId);
    Integer checkTruckTeamNameById(@Param("teamName")String teamName, @Param("tenantId")Integer tenantId);
}