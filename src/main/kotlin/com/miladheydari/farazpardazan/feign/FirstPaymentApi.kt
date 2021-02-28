package com.miladheydari.farazpardazan.feign

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(name = "firstPaymentApi", url = "https://first-payment-provider/payments/")
interface FirstPaymentApi {

    @PostMapping("transfer")
    fun postTransaction(@RequestBody firstPaymentApiPostModel: FirstPaymentApiPostModel): Boolean
}
