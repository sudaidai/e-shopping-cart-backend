package com.zm.web.resolver

import com.zm.web.model.response.CartItemResponse
import com.zm.web.service.CartService
import org.springframework.data.domain.Page
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import java.util.*

@Controller
class CartQueryResolver(
    private val cartService: CartService
) {

    @QueryMapping
    fun listCartItems(
        @Argument page: Int,
        @Argument size: Int
    ): Page<CartItemResponse> {
        return cartService.listCartItems(page, size)
    }
}