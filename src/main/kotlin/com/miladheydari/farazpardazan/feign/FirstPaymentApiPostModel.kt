package com.miladheydari.farazpardazan.feign

data class FirstPaymentApiPostModel(
    val source: String, val dest: String, val cvv2: String,
    val expDate: String, val pin: String, val amount: Long
)
