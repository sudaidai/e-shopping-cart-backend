package com.zm.web.model.response

import com.zm.web.model.ProductDTO

data class CartItemResponse(
    val id: Int,
    val product: ProductDTO,
    val quantity: Int,
    val itemPrice: String
)