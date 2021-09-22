package com.ipman.swagger.sample.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author ipipman
 * @version V1.0
 * @date 2021/9/22
 * @Package com.ipman.swagger.sample.config
 * @Description: (Swagger配置)
 * @date 2021/9/22 6:17 下午
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /**
     * 注入SwaggerDocketBean
     */
    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ipman.swagger.sample.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 配置Swagger文档说明
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("API测试文档")
                .description("DEMO项目的接口测试文档")
                .termsOfServiceUrl("http://www.hangge.com")
                .version("1.0")
                .contact(new Contact("ipman",
                        "http://www.ipman.com",
                        "ipipman@163.com"))
                .build();
    }
}
