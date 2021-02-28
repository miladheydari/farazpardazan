package com.miladheydari.farazpardazan.models

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*
import javax.persistence.Entity

@Entity
@Table(name = "token")
class Token(@Id @GeneratedValue val id: Long,
            @ManyToOne(fetch = FetchType.LAZY)
            @JoinColumn(name = "user_id", nullable = false)
            val user: User,
            val token: String,
            @Column(name = "active")
            var active: Boolean = true
) : Audit()