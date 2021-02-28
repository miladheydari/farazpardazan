package com.miladheydari.farazpardazan.component

import com.miladheydari.farazpardazan.feign.MessageApi
import com.miladheydari.farazpardazan.feign.MessageApiModel
import com.miladheydari.farazpardazan.repositories.TransactionSmsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component


@Component
class SmsTask @Autowired constructor(
    private val transactionSmsRepository: TransactionSmsRepository,
    private val messageApi: MessageApi
) {

    @Scheduled(cron = "0,30 * * * * ?")
    fun sendSms() {
        val sms = transactionSmsRepository.findTransactionSmsBySentSuccessIsFalse()
        sms.forEach { transactionSms ->

            try {
                val result = messageApi.sendMessage(MessageApiModel(transactionSms.text, transactionSms.phone))
                if (result)
                    transactionSms.sentSuccess = true
            } catch (throwable: Throwable) {

            }
            transactionSmsRepository.save(transactionSms)
        }
    }

}
