package com.miladheydari.farazpardazan.models

import com.fasterxml.jackson.annotation.JsonIgnore
import java.math.BigDecimal
import javax.persistence.*


@javax.persistence.Entity
@Table(name = "transaction", indexes = [Index(name = "IDX_CREATED_AT", columnList = "created_at")])
class Transaction(
    @Id @GeneratedValue val id: Long,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_card_id", nullable = false)
    val sourceCardNumber: Card,
    val destCardNumber: String,
    @Enumerated(EnumType.STRING)
    var state: TransactionState = TransactionState.FAIL,
    @Column(precision = 19)
    val amount: BigDecimal,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    val user: User
) : Audit()