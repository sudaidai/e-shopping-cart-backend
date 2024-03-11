package com.zm.web.repository.data

import jakarta.persistence.*
import java.math.BigDecimal

// data before class indicates that this class is a data class, providing additional functionality.
// The @Entity annotation is from JPA (Java Persistence API) and is used to mark this class as an entity.
@Entity(name = "cart_item")
data class CartItem(

    // The @Id annotation marks this property as the primary key of the entity.
    // @GeneratedValue specifies the strategy for generating the values of the primary key.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    // The @ManyToOne annotation denotes a many-to-one relationship with the Member entity.
    // @JoinColumn specifies the name of the column used for the mapping.
    @ManyToOne
    @JoinColumn(name = "member_id")
    var member: Member? = null,

    // Another many-to-one relationship, this time with the Product entity.
    @ManyToOne
    @JoinColumn(name = "product_id")
    var product: Product? = null,

    // The quantity of the product in the cart.
    var quantity: Int = 0
) {
    // Function to calculate the item price.
    fun getItemPrice(): BigDecimal {
        // TODO compute prices with Product
        return BigDecimal.ZERO
    }
}

