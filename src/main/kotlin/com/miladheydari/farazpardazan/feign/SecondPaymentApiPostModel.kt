package com.miladheydari.farazpardazan.feign

data class SecondPaymentApiPostModel(
    val sourcePan: String, val targetPan: String, val cvv2: String,
    val expire: String, val pin2: String, val amount: Long
)
