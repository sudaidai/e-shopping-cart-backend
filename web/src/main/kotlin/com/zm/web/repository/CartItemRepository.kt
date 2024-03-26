package com.zm.web.repository

import com.zm.web.repository.data.CartItem
import com.zm.web.repository.data.Member
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface CartItemRepository : JpaRepository<CartItem, Long> {

    /*
     * This method retrieves a page of CartItem entities associated with the specified Member.
     * It utilizes Spring Data JPA query derivation to automatically generate the SQL query based on the method name.
     * The query is constructed to find CartItem entities where the 'member' property matches the provided 'member' parameter.
     * The method supports pagination using the 'Pageable' parameter, allowing control over page size, page number, sorting, etc.
     */
    fun findByMember(member: Member, pageable: Pageable): Page<CartItem>
}