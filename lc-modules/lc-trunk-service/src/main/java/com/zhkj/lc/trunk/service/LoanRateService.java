package com.zhkj.lc.trunk.service;

import com.zhkj.lc.trunk.model.LoanRate;

import java.util.List;

public interface LoanRateService {

    Boolean insertRate(List<LoanRate> list);

    List<LoanRate> selectAllBytenantId(Integer tenantId);

    LoanRate selectById(Integer rateId);

    Boolean updateById(LoanRate loanRate);

    LoanRate selectByWay(LoanRate loanRate);
}
