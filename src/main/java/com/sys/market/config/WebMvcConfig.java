package com.sys.market.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 테스트 환경에서 외부 경로 매핑
@Configuration
@Profile("test")
public class WebMvcConfig implements WebMvcConfigurer {
    @Value("${resources.path}")
    private String resourcePath;
    @Value("${resources.location}")
    private String resourceLocation;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(resourcePath + "**")
                .addResourceLocations("file:///" + resourceLocation);
    }
}
