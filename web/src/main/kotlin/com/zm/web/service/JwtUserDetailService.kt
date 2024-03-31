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

    override fun loadUserByUsername(account: String?): UserDetails {
        val member = account?.let { memberRepository.findByAccount(it) }
            ?: throw UsernameNotFoundException("Member not found with account: $account")

        return User(member.account, member.password, listOf(SimpleGrantedAuthority("MEMBER")))
    }
}