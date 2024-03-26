package com.zm.web.service;

import com.zm.web.repository.CartItemRepository
import com.zm.web.repository.data.CartItem
import com.zm.web.repository.data.Member
import com.zm.web.resolver.GraphQLCartItem
import com.zm.web.resolver.GraphQLProduct
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.util.*

@Service
class CartService(
    private val cartItemRepository: CartItemRepository
) {

    fun getMemberId(): Long {
        return 1L
    }

    fun CartItem.toGraphQLCartItem(): GraphQLCartItem {
        val itemPrice = this.getItemPrice().toString()
        val graphqlProduct = GraphQLProduct(
            id = this.product?.id ?: UUID.randomUUID(),
            name = this.product?.name ?: "",
            price = this.product?.price?.toString() ?: "0",
            description = this.product?.description ?: ""
        )
        return GraphQLCartItem(
            id = this.id!!.toInt(),
            product = graphqlProduct,
            quantity = this.quantity,
            itemPrice = itemPrice
        )
    }

    fun listCartItems(page: Int, size: Int): Page<GraphQLCartItem> {
        return cartItemRepository.findByMember(
            Member(id = getMemberId()), PageRequest.of(page, size)
        ).map { it.toGraphQLCartItem() }
    }
}

