package com.zhkj.lc.oilcard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import javax.servlet.MultipartConfigElement;

@SpringBootApplication
@EnableFeignClients
//@MapperScan("com.zhkj.lc.oilcard.mapper")
@ComponentScan(basePackages = {"com.zhkj.lc.oilcard", "com.zhkj.lc.common.bean"})
public class LcOilcardServiceApplication {
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //单个文件最大
        factory.setMaxFileSize("10240KB"); //KB,MB
        /// 设置总上传数据总大小
        factory.setMaxRequestSize("102400KB");
        return factory.createMultipartConfig();
    }
    public static void main(String[] args) {
        SpringApplication.run(LcOilcardServiceApplication.class, args);
        System.out.println("-oilcard-启-动-成-功-");
    }
}
