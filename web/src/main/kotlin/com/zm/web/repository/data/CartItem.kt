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
    // This function calculates and returns the item price using BigDecimal arithmetic.
    fun getItemPrice(): BigDecimal {
        // Create a BigDecimal instance representing the quantity of the product.
        // This ensures precise arithmetic, especially for decimal values.
        val quantityBigDecimal = BigDecimal(quantity)

        // Use the non-null assertion (!! operator) to assert that 'product' is not null.
        // This is appropriate if 'product' is guaranteed to be non-null in this context.
        // If there's a chance of 'product' being null, additional null checks should be considered.
        val productPrice = product!!.price

        // Multiply the quantity by the product price to get the total item price.
        return quantityBigDecimal.multiply(productPrice)
    }
}

