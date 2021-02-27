package com.zhkj.lc.trunk.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.trunk.model.Contract;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 合同管理 数据层
 * 
 * @author zhkj
 * @date 2018-11-28
 */
public interface ContractMapper extends BaseMapper<Contract> {

    Boolean insertContract(Contract contract);
    public Integer deleteByIds(Contract contract);

    public List<Object> selectPageList(Query<Object> objectQuery,Contract contract);

    public List<Contract> selectByIds(Contract contract);

    public List<Contract> selectAllUnexpiredContract(Contract contract);

    public List<Contract> selectAll(Contract contract);

    Contract selectByContractNumber(Contract contract);

    Integer countNoFinishContract(@Param("truckTeamIds") String[] truckTeamIds, @Param("tenantId") Integer tenantId);
}