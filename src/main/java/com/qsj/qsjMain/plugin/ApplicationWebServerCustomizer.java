package com.qsj.qsjMain.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.core.Ordered;

/**
 * 定制 Embedded WebServer，比参数化配置更加灵活，建议统一在这里设置
 */
public class ApplicationWebServerCustomizer
        implements WebServerFactoryCustomizer<ConfigurableWebServerFactory>, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationWebServerCustomizer.class);

    @Override
    public void customize(ConfigurableWebServerFactory factory) {
    }

    /**
     * 在 SpringBoot 内置的 {@link WebServerFactoryCustomizer} 之后执行
     */
    @Override
    public int getOrder() {
        return 100;
    }

}
