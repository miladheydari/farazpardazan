package com.miladheydari.farazpardazan.feign

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(name = "messageApi", url = "https://sms-provider/messages/")
interface MessageApi {

    @PostMapping("send-sms")
    fun sendMessage(@RequestBody messageApiModel: MessageApiModel): Boolean
}
