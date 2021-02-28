package com.miladheydari.farazpardazan.feign

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(name = "secondPaymentApi", url = "https://second-payment-provider/cards/")
interface SecondPaymentApi {

    @PostMapping("pay")
    fun postTransaction(@RequestBody secondPaymentApiPostModel: SecondPaymentApiPostModel): Boolean
}
