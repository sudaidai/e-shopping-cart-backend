package com.zm.web.resolver

import com.zm.web.model.response.CartResponse
import com.zm.web.service.CartService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import java.util.*

@Controller
class CartQueryResolver(
    private val cartService: CartService
) {

    @QueryMapping
    fun cart(
        @Argument id: UUID,
        @Argument("currency") currencyInput: CurrencyInput
    ): CartResponse {
        return cartService.queryCart(id, currencyInput)
    }
}

data class CurrencyInput(
    val code: String
)