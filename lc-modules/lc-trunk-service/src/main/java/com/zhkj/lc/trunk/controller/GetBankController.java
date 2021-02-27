package com.zhkj.lc.trunk.controller;

import com.zhkj.lc.trunk.utils.BankcardNo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
@RestController
@RequestMapping("getBankController")
public class GetBankController {
    /**
     * 获取银行账号所属行
     * @param bankCardNumber
     * @return
     */
    @PostMapping("/getBank")
    public Map<String,String> getBank(String bankCardNumber) {
        return BankcardNo.getCardDetail(bankCardNumber);
    }
}
