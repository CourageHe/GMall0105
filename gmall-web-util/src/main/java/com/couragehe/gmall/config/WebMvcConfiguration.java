package com.couragehe.gmall.config;

import com.couragehe.gmall.interceptors.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @PackageName:com.couragehe.gmall.interceptors
 * @ClassName:WebMvcConfiguration
 * @Description:用户拦截器配置
 * @Autor:CourageHe
 * @Date: 2020/1/16 20:16
 */
@Configuration
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(authInterceptor).addPathPatterns("/**").excludePathPatterns("/error");
        //排除拦截路径
//        registry.addInterceptor(authInterceptor).excludePathPatterns("/*error");
        super.addInterceptors(registry);
    }





}
