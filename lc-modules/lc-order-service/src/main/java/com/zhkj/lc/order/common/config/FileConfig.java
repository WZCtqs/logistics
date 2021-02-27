package com.zhkj.lc.order.common.config;

import com.zhkj.lc.common.config.Global;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @Auther: HP
 * @Date: 2019/4/24 16:42
 * @Description:
 */
@Configuration
public class FileConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        //文件磁盘图片url 映射
        //配置server虚拟路径，handler为前台访问的目录，locations为files相对应的本地路径
        registry.addResourceHandler("/file/**").addResourceLocations("file:"+ Global.FILE_PATH_ORDER);
    }

}
