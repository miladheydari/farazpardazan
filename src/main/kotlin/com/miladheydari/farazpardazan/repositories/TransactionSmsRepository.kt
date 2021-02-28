package com.miladheydari.farazpardazan.repositories

import com.miladheydari.farazpardazan.models.Sms
import com.miladheydari.farazpardazan.models.TransactionSms
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TransactionSmsRepository : JpaRepository<TransactionSms, Long> {
    fun findTransactionSmsBySentSuccessIsFalse(): List<TransactionSms>

}