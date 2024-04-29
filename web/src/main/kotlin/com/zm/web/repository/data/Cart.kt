package com.zm.web.repository.data

import com.zm.web.constant.Currency
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.Instant

@Entity
@Table(name = "cart")
class Cart (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @OneToMany(mappedBy = "cart", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val cartItems: MutableList<Item> = mutableListOf(),

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    var isAbandoned: Boolean = false,

    @Enumerated(value = EnumType.STRING)
    var currency: Currency? = null,

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
}