package com.zm.web.resolver

import com.zm.web.service.CartService
import org.springframework.data.domain.Page
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import java.util.*

@Controller
class CartResolver (
    private val cartService: CartService
){

    @QueryMapping
    fun listCartItems(
        @Argument page: Int,
        @Argument size: Int
    ): Page<GraphQLCartItem> {
        return cartService.listCartItems(page, size)
    }
}

data class GraphQLCartItem(
    val id: Int,
    val product: GraphQLProduct,
    val quantity: Int,
    val itemPrice: String
)

data class GraphQLProduct(
    val id: UUID,
    val name: String,
    val price: String,
    val description: String
)