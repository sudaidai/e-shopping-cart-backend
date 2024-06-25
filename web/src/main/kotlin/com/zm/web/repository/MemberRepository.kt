package com.zm.web.repository

import com.zm.web.repository.data.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long> {

    fun findByAccount(account: String): Member?

    fun existsByAccount(account: String): Boolean
}