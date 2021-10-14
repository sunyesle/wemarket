package com.sys.market.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(title = "WeMarket API Documentation",
        description = "WeMarket 서버 API에 대한 연동 문서입니다."
    )
)
public class SwaggerConfig {
}