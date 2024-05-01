package com.zm.web.repository.data

import com.zm.web.constant.Currency
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.GenericGenerator
import java.time.Instant
import java.util.*

@Entity
@Table(name = "cart")
class Cart (
    @Id
    @GeneratedValue
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(columnDefinition = "BINARY(16)")
    var id: UUID? = null,

    @OneToOne
    @JoinColumn(name = "member_id")
    var member: Member? = null,

    @OneToMany(mappedBy = "cart", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    val cartItems: MutableList<Item> = mutableListOf(),

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    var isAbandoned: Boolean = false,

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
}