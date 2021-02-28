package com.miladheydari.farazpardazan.models

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*
import javax.persistence.Entity

@Entity
@Table(name = "sms")
class Sms(@Id @GeneratedValue val id: Long,
          @ManyToOne(fetch = FetchType.LAZY)
          @JoinColumn(name = "user_id", nullable = false)
          val user: User,
          val code: String,
          @Column(name = "active")
          var active: Boolean = true
) : Audit()