package com.zm.web.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement

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
            .title("Shopping Cart Api")
            .description("The API documentation for shopping cart")
            .version("1.0.0")
    }
}