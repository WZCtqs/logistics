package com.zhkj.lc.trunk.controller;

import com.zhkj.lc.trunk.model.LoanRate;
import com.zhkj.lc.trunk.service.LoanRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/loanRate")
public class LoanRateController {
    @Autowired
    private LoanRateService loanRateService;

    @PostMapping("insert")
    public Boolean insertRate(@RequestBody Integer tenantId){
        List<LoanRate> list = new ArrayList<>();
        String[] labels={"1期还款利率","3期还款利率","6期还款利率","12期还款利率","24期还款利率","36期还款利率"};
        for(int i = 0;i < 6; i++){
            LoanRate loanRate = new LoanRate();
            loanRate.setLabel(labels[i]);
            loanRate.setRate(0.0);
            loanRate.setSort(i);
            loanRate.setTenantId(tenantId);
            list.add(loanRate);
        }
        return loanRateService.insertRate(list);
    }

}
