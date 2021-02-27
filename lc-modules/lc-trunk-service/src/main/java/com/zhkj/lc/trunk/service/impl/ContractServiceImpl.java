package com.zhkj.lc.trunk.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.support.Convert;
import com.zhkj.lc.trunk.mapper.*;
import com.zhkj.lc.trunk.model.*;
import com.zhkj.lc.trunk.service.IContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 合同管理 服务层实现
 * 
 * @author zhkj
 * @date 2018-11-28
 */
@Service
public class ContractServiceImpl extends ServiceImpl<ContractMapper,Contract> implements IContractService {
    @Autowired
    ContractMapper contractMapper;
    @Autowired
    TruDriverMapper truDriverMapper;
    @Autowired
    TruTruckTeamMapper truTruckTeamMapper;

    @Override
    public boolean deleteByIds(String contractNumbers) {
        String[] str = contractNumbers.split(",");
        Contract contract = new Contract();
        contract.setContractNumbers(str);
        contract.setUpdateTime(new Date());
        /*oilApply.setUpdateBy("");*/
        Integer result = contractMapper.deleteByIds(contract);
        return null != result && result >= 1;
    }

    @Override
    public List<Contract> selectByIds(String contractNumbers) {
        String[] str = contractNumbers.split(",");
        Contract contract = new Contract();
        contract.setContractNumbers(str);
        return contractMapper.selectByIds(contract);
    }

    @Override
    public List<Contract> selectAllUnexpiredContract(Contract contract) {
        return contractMapper.selectAllUnexpiredContract(contract);
    }

    @Override
    public List<Contract> selectAll(Contract contract) {
        return contractMapper.selectAll(contract);
    }

    @Override
    public Contract selectByContractNumber(Contract contract) {
        return contractMapper.selectByContractNumber(contract);
    }

    @Override
    public Page selectPageList(Query objectQuery, Contract contract) {
        List<Contract> contractsList = contractMapper.selectPageList(objectQuery, contract);
        objectQuery.setRecords(contractsList);
        return objectQuery;
    }

    @Override
    public Integer countNoFinishContract(String truckTeamIds, Integer tenantId){
        return contractMapper.countNoFinishContract(Convert.toStrArray(truckTeamIds), tenantId);
    }

    @Override
    public Boolean insertContract(Contract contract) {
        return contractMapper.insertContract(contract);
    }

}
