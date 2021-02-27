package com.zhkj.lc.trunk.service.impl;

import com.zhkj.lc.trunk.mapper.LoanRateMapper;
import com.zhkj.lc.trunk.model.Loan;
import com.zhkj.lc.trunk.model.LoanRate;
import com.zhkj.lc.trunk.service.LoanRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanRateServiceImpl implements LoanRateService {
    private final LoanRateMapper loanRateMapper;

    @Autowired
    public LoanRateServiceImpl(LoanRateMapper loanRateMapper) {
        this.loanRateMapper = loanRateMapper;
    }

    @Override
    public Boolean insertRate(List<LoanRate> list) {
        return loanRateMapper.insertRate(list);
    }

    @Override
    public List<LoanRate> selectAllBytenantId(Integer tenantId) {
        return loanRateMapper.selectAllBytenantId(tenantId);
    }

    @Override
    public LoanRate selectById(Integer rateId) {
        return loanRateMapper.selectById(rateId);
    }

    @Override
    public Boolean updateById(LoanRate loanRate) {
        return loanRateMapper.updateById(loanRate);
    }

    @Override
    public LoanRate selectByWay(LoanRate loanRate) {
        return loanRateMapper.selectByWay(loanRate);
    }
}
