package com.zm.web.config

import com.zm.web.handler.JwtAuthenticationEntryPoint
import com.zm.web.filter.JwtAuthenticationFilter
import com.zm.web.service.JwtUserDetailService
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

// Defines JWT as the security scheme for the API
@SecurityScheme(
    name = "Authorization",
    type = SecuritySchemeType.APIKEY,
    `in` = SecuritySchemeIn.HEADER
)
@Configuration
class SecurityConfiguration(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint
) {
    
    companion object {
        private val UNAUTHORIZED_ROUTE = arrayOf(
            "/graphql",
            "/graphiql",
            "/v3/api-docs",
            "/v3/api-docs/swagger-config",
            "/swagger-ui/**",
            "/api/auth",
            "/api/member",
            "/api/home"
        )
    }

    /**
     * Configures the security filter chain for the application.
     * This method defines authorization rules, session management, CORS configuration, CSRF protection, and filters.
     * @param http HttpSecurity object used to configure security settings.
     * @return SecurityFilterChain containing configured security filters.
     */
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain? {
        // Configure authorization rules
        http.authorizeHttpRequests { authorize ->
            authorize
                // Permit access to unauthorized routes
                .requestMatchers(*UNAUTHORIZED_ROUTE).permitAll()
                // Require authentication for any other request
                .anyRequest().authenticated()
        }.exceptionHandling {
            exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint)
        }

        // Configure remember-me functionality
        http.rememberMe(Customizer.withDefaults())

        // Configure session management to use stateless sessions
        http.sessionManagement { sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }

        // Configure CORS (Cross-Origin Resource Sharing)
        http.cors { cors -> cors.configurationSource(corsConfigurationSource()) }

        // Disable CSRF (Cross-Site Request Forgery) protection
        http.csrf { csrf -> csrf.disable() }

        // Add the JWT authentication filter before the BasicAuthenticationFilter in the filter chain
        http.addFilterBefore(jwtAuthenticationFilter, BasicAuthenticationFilter::class.java)

        // Build and return the SecurityFilterChain
        return http.build()
    }

    /**
     * Defines a bean for PasswordEncoder, which is used to encode passwords.
     * @return PasswordEncoder bean for encoding passwords.
     */
    @Bean
    fun passwordEncoder(): PasswordEncoder? {
        // Create and return an instance of BCryptPasswordEncoder for password encoding
        return BCryptPasswordEncoder()
    }

    /**
     * Defines a bean for UserDetailsService, which is responsible for loading user-specific data.
     * @return UserDetailsService bean for loading user-specific data.
     */
    @Bean
    fun userDetailsService(): UserDetailsService? {
        // Create and return an instance of JwtUserDetailService for providing user-specific data
        return JwtUserDetailService()
    }


    /**
     * AuthenticationManager is responsible for authenticating a user based on provided credentials.
     * It delegates the authentication process to one or more AuthenticationProviders.
     * Spring Security offers several implementations of AuthenticationManager, commonly using ProviderManager.
     * ProviderManager delegates authentication requests to a chain of AuthenticationProviders until one successfully authenticates the user.
     * To authenticate() method of AuthenticationManager is invoked during authentication to attempt user authentication.
     */
    @Bean
    @Throws(Exception::class)
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager? {
        return config.authenticationManager
    }

    /**
     * AuthenticationProvider is an interface defining the contract for user authentication.
     * Spring Security provides various AuthenticationProvider implementations, each capable of authenticating users via different mechanisms (e.g., database, LDAP, OAuth, etc.).
     * One commonly used implementation is DaoAuthenticationProvider, which authenticates users based on credentials stored in a database.
     * When Spring Security authenticates a user, it iterates over configured AuthenticationProviders until one successfully authenticates the user.
     * To authenticate() method of AuthenticationProvider receives an Authentication object (usually a UsernamePasswordAuthenticationToken), validates the credentials, and returns an authenticated Authentication object if successful.
     */
    @Bean
    fun authenticationProvider(): AuthenticationProvider? {
        val authenticationProvider = DaoAuthenticationProvider()
        authenticationProvider.setUserDetailsService(userDetailsService())
        authenticationProvider.setPasswordEncoder(passwordEncoder())
        return authenticationProvider
    }

    /**
     * Generates a CorsConfigurationSource, which defines CORS (Cross-Origin Resource Sharing) configuration for the application.
     * This method sets up CORS to allow requests from all origins, with specified HTTP methods and headers.
     * It also allows credentials to be included in cross-origin requests and sets the maximum age of pre-flight requests.
     * @return CorsConfigurationSource containing CORS configuration for the application.
     */
    private fun corsConfigurationSource(): CorsConfigurationSource? {
        // Create a new CorsConfiguration object
        val configuration = CorsConfiguration()

        // Allow requests from all origins
        configuration.addAllowedOriginPattern("*")

        // Define allowed HTTP methods for cross-origin requests
        configuration.allowedMethods = mutableListOf("GET", "POST", "PATCH", "PUT", "DELETE")

        // Define allowed HTTP headers for cross-origin requests
        configuration.allowedHeaders =
            mutableListOf("Origin", "X-Requested-With", "Content-Type", "Accept", "Cache-Control")

        // Allow credentials to be included in cross-origin requests
        configuration.allowCredentials = true

        // Set the maximum age (in seconds) of pre-flight requests
        configuration.maxAge = 3600L

        // Create a UrlBasedCorsConfigurationSource to register CORS configuration for specific paths
        val source = UrlBasedCorsConfigurationSource()

        // Register the CORS configuration for all paths (/**)
        source.registerCorsConfiguration("/**", configuration)

        // Return the CorsConfigurationSource containing CORS configuration
        return source
    }

}
