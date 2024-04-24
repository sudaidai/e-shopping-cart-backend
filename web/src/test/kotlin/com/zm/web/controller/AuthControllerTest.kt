package com.zm.web.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.zm.web.model.request.LoginRequest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class AuthControllerTests : ControllerAbstractTest() {

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `test login with valid credentials`() {
        val loginRequest = LoginRequest(account = NO_ROLE_USER, password = "123456")

        mockMvc.perform(
            post("/api/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        ).andExpect(status().isOk)
    }

    @Test
    fun `test login with invalid credentials`() {
        val loginRequest = LoginRequest(account = NO_ROLE_USER, password = "invalidPassword")

        mockMvc.perform(
            post("/api/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        ).andExpect(status().isUnauthorized)
    }
}