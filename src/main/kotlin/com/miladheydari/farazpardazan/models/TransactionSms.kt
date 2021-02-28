package com.miladheydari.farazpardazan.models

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*
import javax.persistence.Entity

@Entity
@Table(name = "sms")
class TransactionSms(
    @Id @GeneratedValue val id: Long,
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", nullable = false)
    val transaction: Transaction,
    val text: String,
    val phone:String,
    var sentSuccess: Boolean = false
) : Audit()