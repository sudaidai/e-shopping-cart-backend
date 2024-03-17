package com.zm.web.repository

import com.zm.web.repository.data.CartItem
import org.springframework.data.jpa.repository.JpaRepository

interface CartItemRepository : JpaRepository<CartItem, Long> {
}