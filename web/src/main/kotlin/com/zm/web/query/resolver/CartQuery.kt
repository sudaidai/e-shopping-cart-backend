package com.zm.web.query.resolver

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.operations.Query
import com.zm.web.query.data.Cart
import com.zm.web.query.data.CartItem
import com.zm.web.query.data.Member
import com.zm.web.query.data.Product
import com.zm.web.repository.CartItemRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class CartQuery(private val repository: CartItemRepository) : Query {
    fun cart(@GraphQLIgnore @Autowired repository: CartItemRepository, id: Int): Cart {
        val item1 = CartItem(ID("1"), Member(ID("1")), Product(ID("1")), 10)
        return Cart(ID("uuid"), listOf(item1));
    }
}
