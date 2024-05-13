package com.zm.web.resolver

import com.zm.web.model.response.CartResponse
import com.zm.web.service.CartService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.stereotype.Controller

@Controller
class CartMutationResolver(
    private val cartService: CartService
) {
    @MutationMapping
    fun addItemToCart(
        @Argument productId: String,
        @Argument quantity: Int
    ): CartResponse {
        return cartService.addItemToCart(productId, quantity)
    }

    @MutationMapping
    fun updateCartItem(
        @Argument cartItemId: String,
        @Argument quantity: Int
    ): CartResponse {
        return cartService.updateCartItem(cartItemId, quantity)
    }

    @MutationMapping
    fun removeCartItem(
        @Argument cartItemId: String
    ): CartResponse {
        return cartService.removeCartItem(cartItemId)
    }

    @MutationMapping
    fun clearCart(
        @Argument id: String
    ): CartResponse {
        return cartService.clearCart()
    }
}
