package com.zhkj.lc.trunk.mapper;

import com.zhkj.lc.trunk.model.LoanRate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRateMapper {

    Boolean insertRate(List<LoanRate> list);

    List<LoanRate> selectAllBytenantId(Integer tenantId);

    LoanRate selectById(Integer rateId);

    LoanRate selectByWay(LoanRate loanRate);

    Boolean updateById(LoanRate loanRate);
}
