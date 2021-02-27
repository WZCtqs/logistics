package com.zhkj.lc.trunk.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.trunk.model.Contract;

import java.util.List;

/**
 * 合同管理 服务层
 * 
 * @author zhkj
 * @date 2018-11-28
 */
public interface IContractService extends IService<Contract>
{
    public boolean deleteByIds(String contractNumbers);

    public Page selectPageList(Query<Object> objectQuery, Contract contract);

    public List<Contract> selectByIds(String contractNumbers);

    public List<Contract> selectAllUnexpiredContract(Contract contract);

    public List<Contract> selectAll(Contract contract);

    Contract selectByContractNumber(Contract contract);

    Integer countNoFinishContract(String truckTeamIds, Integer tenantId);

    Boolean insertContract(Contract contract);

}
