package com.zm.web.query.data

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.federation.directives.ContactDirective
import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.Schema
import com.expediagroup.graphql.server.operations.Query
import org.springframework.stereotype.Component

@ContactDirective(
    name = "Cart",
    url = "https://www.google.com",
    description = "Shopping cart in graphQL"
)
@GraphQLDescription("This is a shopping cart")
@Component
class CartSchema : Schema

data class Cart(val id: ID, val items: List<com.zm.web.query.data.CartItem>)

data class CartItem(val id: ID, val member: Member, val product: Product, val quantity: Int)

data class Member(val id: ID)

data class Product(val id: ID)
