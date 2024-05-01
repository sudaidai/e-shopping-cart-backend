package com.zm.web.resolver

import com.zm.web.service.CartService
import org.springframework.stereotype.Controller

@Controller
class CartMutationResolver(
    private val cartService: CartService
) {
}