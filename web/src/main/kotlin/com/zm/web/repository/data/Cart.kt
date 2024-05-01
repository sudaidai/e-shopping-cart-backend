package com.zm.web.repository.data

import com.zm.web.constant.Currency
import com.zm.web.model.response.CartResponse
import com.zm.web.model.response.CurrencyDTO
import com.zm.web.model.response.PriceDTO
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.math.BigDecimal
import java.time.Instant
import java.util.*

@Entity
@Table(name = "cart")
class Cart (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @OneToOne
    @JoinColumn(name = "member_id")
    var member: Member? = null,

    @OneToMany(mappedBy = "cart", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    val cartItems: MutableList<Item> = mutableListOf(),

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    var isAbandoned: Boolean = false,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var currency: Currency? = Currency.USD,

    var notes: String? = null,

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

    fun toCartResponse(currency: Currency, subTotal: BigDecimal, shippingTotal: BigDecimal, taxTotal: BigDecimal, grandTotal: BigDecimal) = CartResponse(
        id = id.toString(),
        email = member?.email.orEmpty(),
        isEmpty = cartItems.isEmpty(),
        abandoned = isAbandoned,
        totalItems = cartItems.sumOf { it.quantity },
        totalUniqueItems = cartItems.size,
        currency = CurrencyDTO(currency.name, currency.getSymbol()),
        subTotal = PriceDTO(subTotal, "${currency.getSymbol()}${subTotal.toPlainString()}"),
        shippingTotal = PriceDTO(shippingTotal, "${currency.getSymbol()}${shippingTotal.toPlainString()}"),
        taxTotal = PriceDTO(taxTotal, "${currency.getSymbol()}${taxTotal.toPlainString()}"),
        grandTotal = PriceDTO(grandTotal, "${currency.getSymbol()}${grandTotal.toPlainString()}"),
        attributes = emptyList(),
        notes = notes,
        createdAt = createTime?.toString().orEmpty(),
        updatedAt = updateTime?.toString().orEmpty(),
        items = cartItems.map { it.toItemDTO(currency) }
    )
}