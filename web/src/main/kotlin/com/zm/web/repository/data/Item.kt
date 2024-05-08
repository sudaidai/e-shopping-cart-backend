package com.zm.web.repository.data

import com.zm.web.constant.Currency
import com.zm.web.model.response.ItemDTO
import com.zm.web.model.response.PriceDTO
import com.zm.web.utils.TimeUtils
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.PreUpdate
import jakarta.persistence.Table
import jakarta.persistence.Temporal
import jakarta.persistence.TemporalType
import jakarta.persistence.UniqueConstraint
import org.hibernate.annotations.CreationTimestamp
import java.math.BigDecimal
import java.time.Instant

// data before class indicates that this class is a data class, providing additional functionality.
// The @Entity annotation is from JPA (Java Persistence API) and is used to mark this class as an entity.
@Entity
@Table(name = "item",
    uniqueConstraints = [UniqueConstraint(columnNames = ["member_id", "product_id"])])
data class Item(

    // The @Id annotation marks this property as the primary key of the entity.
    // @GeneratedValue specifies the strategy for generating the values of the primary key.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    var cart: Cart? = null,

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
    var quantity: Int = 0,

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    var createTime: Instant? = null,

    @Temporal(TemporalType.TIMESTAMP)
    var updateTime: Instant? = null
) {
    @PreUpdate
    fun preUpdate() {
        updateTime = Instant.now()
    }

    fun toItemDTO(currency: Currency): ItemDTO {
        val productPrice = getProductPrice()
        val itemPrice = getItemPrice()

        return ItemDTO(
            id = this.id!!.toString(),
            name = product?.name ?: "Unknown Product",
            description = product?.description,
            images = product?.images?.split(",") ?: listOf(),
            quantity = quantity,
            attributes = listOf(),
            unitTotal = PriceDTO(productPrice, currency.getSymbol() + productPrice),
            lineTotal = PriceDTO(itemPrice, currency.getSymbol() + itemPrice),
            createdAt = TimeUtils.formatInstant(createTime),
            updatedAt = TimeUtils.formatInstant(updateTime)
        )
    }

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

    fun addQuantity(quantity: Int) {
        this.quantity = this.quantity + quantity
    }

    fun reduceQuantity(quantity: Int) {
        val reducedQuantity = this.quantity - quantity
        this.quantity = if (reducedQuantity >= 0) reducedQuantity else 0
    }

    private fun getProductPrice(): BigDecimal{
        return product?.price ?: BigDecimal.ZERO
    }
}

