package com.zm.web.repository

import com.zm.web.repository.data.Cart
import com.zm.web.repository.data.Item
import org.springframework.data.jpa.repository.JpaRepository

interface ItemRepository : JpaRepository<Item, Long> {

    fun countByCart(cart: Cart): Int
}