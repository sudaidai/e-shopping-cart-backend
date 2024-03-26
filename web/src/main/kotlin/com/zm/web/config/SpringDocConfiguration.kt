package com.zm.web.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement

/**
 * Configuration for Swagger.
 * Access the Swagger UI at {server}/swagger-ui/index.html
 */
@Configuration
class SpringDocConfiguration {

    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
            .addSecurityItem(SecurityRequirement().addList("Authorization"))
            .info(getInfo())
    }

    private fun getInfo(): Info {
        return Info()
            .title("Shopping Cart API")
            .description("API documentation for the shopping cart service.")
            .version("1.0.0")
    }
}
