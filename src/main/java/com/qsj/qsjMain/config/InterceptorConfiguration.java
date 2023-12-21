package com.qsj.qsjMain.config;

import com.qsj.qsjMain.interceptor.GlobalInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {

    private final GlobalInterceptor globalInterceptor;

    public InterceptorConfiguration(GlobalInterceptor globalInterceptor) {
        this.globalInterceptor = globalInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration registration = registry.addInterceptor(globalInterceptor);
        registration.addPathPatterns("/**");
        registration.excludePathPatterns("/api/v1/user/login",
                "/api/v1/order/payNotify", "/api/v1/order/deliveryCallback", "/api/v1/user/msgNotify",
                "/api/v1/shop/**", "/api/v1/_internalBiz/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*.lightmeal-service.com")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }


}
