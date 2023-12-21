package com.qsj.qsjMain.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;


@EnableWebMvc
@EnableSwagger2
@Configuration
public class SwaggerConfiguration{
        @Bean
        public Docket createRestApi() {
            List<RequestParameter> globalRequestParameters = List.of(
                    new RequestParameterBuilder()
                            .name("x-priv-credential")
                            .description("管理员凭据，和x-user-id一起使用，可以指定任意用户访问任意接口【仅开发调试使用！】")
                            .in(ParameterType.HEADER)
                            .required(false)
                            .build(),
                    new RequestParameterBuilder()
                            .name("x-user-id")
                            .description("目标用户id【仅开发调试使用！】")
                            .in(ParameterType.HEADER)
                            .required(false)
                            .build(),
                    new RequestParameterBuilder()
                            .name("x-credential")
                            .description("用户凭据")
                            .in(ParameterType.HEADER)
                            .required(false)
                            .build()
            );
            return new Docket(DocumentationType.SWAGGER_2)
                    .apiInfo(apiInfo())
                    .select()
                    .apis(RequestHandlerSelectors.basePackage("com.qsj.qsjMain.controller"))
                    .paths(PathSelectors.any())
                    .build().globalRequestParameters(globalRequestParameters);
        }
        private ApiInfo apiInfo() {
            return new ApiInfoBuilder()
                    .title("qsj service api")
                    .description("x-credential: 用户凭据，x-user-id: 用户id，x-priv-credential: 管理员凭据\n参考内部文档查看凭据")
                    .version("0.0.1")
                    .build();
        }

}
