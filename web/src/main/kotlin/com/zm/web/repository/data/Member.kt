package com.zm.web.repository.data

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.Instant

@Entity
@Table(name = "member")
data class Member(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(unique = true, nullable = false)
    var account: String? = null,

    var email: String? = null,

    @Column(nullable = false)
    var password: String? = null,

    var name: String? = null,

    var country: String? = null,

    var address: String? = null,

    var phone: String? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    var cart: Cart? = null,

    @Column(name = "cart_id", insertable = false, updatable = false)
    var cartId: Long? = null,

    @Column(nullable = true)
    var isDelete: Boolean? = false,

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