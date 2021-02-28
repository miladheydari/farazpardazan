package com.miladheydari.farazpardazan

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.scheduling.annotation.EnableScheduling

@EnableFeignClients
@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
class FarazpardazanApplication

fun main(args: Array<String>) {
    runApplication<FarazpardazanApplication>(*args)
}
