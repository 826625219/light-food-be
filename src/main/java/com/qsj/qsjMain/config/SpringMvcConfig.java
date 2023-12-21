package com.qsj.qsjMain.config;

import com.qsj.qsjMain.plugin.ApplicationWebServerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring MVC 配置
 */
@Configuration
class SpringMvcConfig {

    @Bean
    ApplicationWebServerCustomizer applicationWebServerCustomizer() {
        return new ApplicationWebServerCustomizer();
    }

}
