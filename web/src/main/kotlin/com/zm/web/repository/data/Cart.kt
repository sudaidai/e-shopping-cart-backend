package com.zm.web.repository.data

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.Instant
import java.util.*

@Entity
@Table(name = "cart")
class Cart (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(unique = true, columnDefinition = "BINARY(16)")
    var uuid: UUID? = null,

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