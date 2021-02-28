package com.miladheydari.farazpardazan.models

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*


@javax.persistence.Entity
@Table(name = "card")
class Card(
    @Id @GeneratedValue val id: Long,
    val number: String,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    var user: User,
    var active: Boolean = true
) : Audit()