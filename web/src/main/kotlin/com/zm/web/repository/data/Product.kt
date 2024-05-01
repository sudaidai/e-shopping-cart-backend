package com.zm.web.repository.data

import com.zm.web.constant.ProductStatus
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Type
import java.math.BigDecimal
import java.time.Instant
import java.util.*

@Entity
@Table(name = "product")
data class Product(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(unique = true, columnDefinition = "BINARY(16)")
    var uuid: UUID? = null,

    @Column(unique = true, length = 50)
    var name: String? = null,

    var price: BigDecimal? = BigDecimal.ZERO,

    var description: String? = null,

    var images: String? = null,

    @Enumerated(EnumType.STRING)
    var status: ProductStatus? = null,

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    var createTime: Instant? = null,

    @Temporal(TemporalType.TIMESTAMP)
    var updateTime: Instant? = null,

    var tookOffTime: Instant? = null,

    var listedTime: Instant? = null
) {
    @PrePersist
    fun prePersist() {
        if (uuid == null) {
            uuid = UUID.randomUUID()
        }
        updateTime = createTime
    }

    @PreUpdate
    fun preUpdate() {
        updateTime = Instant.now()
    }
}