package com.example.matchingbab.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI matchingBabOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Matching-Bab API")
                        .description("밥약 매칭 서비스 API 문서")
                        .version("v1"));
    }
}
