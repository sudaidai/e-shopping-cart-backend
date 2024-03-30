package com.zm.web.service

import com.zm.web.repository.MemberRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException

class JwtUserDetailService : UserDetailsService {

    private lateinit var memberRepository: MemberRepository

    @Autowired
    fun setMemberRepository(memberRepository: MemberRepository) {
        this.memberRepository = memberRepository
    }

    override fun loadUserByUsername(name: String?): UserDetails {
        val member = name?.let { memberRepository.findByName(it) }
            ?: throw UsernameNotFoundException("Member not found with name: $name")

        return User(member.name, member.password, listOf(SimpleGrantedAuthority("member")))
    }
}