package com.zm.web.resolver

import com.zm.web.model.request.CartItemUpdateRequest
import com.zm.web.model.response.CartItemResponse
import com.zm.web.service.CartService
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.stereotype.Controller
import java.util.*

@Controller
class CartMutationResolver(
    private val cartService: CartService
) {

    @MutationMapping
    fun addToCart(productId: UUID, quantity: Int): CartItemResponse {
        return cartService.addToCart(productId, quantity)
    }

    @MutationMapping
    fun clearCart(): Boolean {
        return cartService.clearCart()
    }

    @MutationMapping
    fun updateCart(updateCartRequest: List<CartItemUpdateRequest>): List<CartItemResponse> {
        return cartService.updateCart(updateCartRequest)
    }
}