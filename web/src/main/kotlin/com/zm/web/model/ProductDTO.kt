package com.zm.web.model

import java.util.*

data class ProductDTO (
    val id: UUID,
    val name: String,
    val price: String,
    val description: String
)