package com.zhkj.lc.trunk.utils.wechat.start;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InterfaceUrlIntiRunner implements CommandLineRunner {
    private static final long serialVersionUID = 1L;

    @Override
    public void run(String... args){
        try {
            InterfaceUrlInti.init();
        }catch (Exception e){
        }
    }
}
