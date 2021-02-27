package com.zhkj.lc.trunk.common.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @Auther: HP
 * @Date: 2019/4/3 08:25
 * @Description:
 */
@Component
public class HttpClientBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        if (containsBeanDefinition(beanFactory, "apacheHttpClientFactory", "traceHttpClientBuilder")) {
            BeanDefinition bd = beanFactory.getBeanDefinition("apacheHttpClientFactory");
            bd.setDependsOn("traceHttpClientBuilder");
        }

    }

    private boolean containsBeanDefinition(ConfigurableListableBeanFactory beanFactory, String... beans) {
        return Arrays.stream(beans).allMatch(b -> beanFactory.containsBeanDefinition(b));
    }
}
