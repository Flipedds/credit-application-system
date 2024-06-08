package me.dio.credit.application.system.configs

import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    @Bean
    fun publicApi(): GroupedOpenApi? {
        return GroupedOpenApi.builder()
            .displayName("Credit-application-System")
            .group("credit-application-system")
            .pathsToMatch("/credits/**", "/customers/**")
            .build()
    }
}