package com.zm.web.model.request

import com.zm.web.constant.CartAction
import java.util.UUID

data class CartItemUpdateRequest(
    val productId: UUID?,
    val cartItemId: Long?,
    val quantity: Int?,
    val action: CartAction
)