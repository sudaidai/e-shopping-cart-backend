package com.zm.web.controller

import com.zm.web.controller.ControllerAbstractTest.Companion.NO_ROLE_USER
import com.zm.web.repository.MemberRepository
import com.zm.web.repository.data.Member
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = NO_ROLE_USER)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class ControllerAbstractTest {

    companion object {
        const val NO_ROLE_USER = "testBruce"
    }

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var memberRepository: MemberRepository

    @BeforeAll
    fun setup() {
        if(memberRepository.findByAccount(NO_ROLE_USER) == null) {
            val member = Member(
                account = NO_ROLE_USER,
                password = passwordEncoder.encode("123456")
            )
            memberRepository.save(member)
        }
    }
}