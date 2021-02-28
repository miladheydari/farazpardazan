package com.miladheydari.farazpardazan.repositories

import com.miladheydari.farazpardazan.models.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TransactionRepository : JpaRepository<Transaction, Long> {
    fun findTransactionByIdAndUser(id:Long,user: User): Optional<Transaction>

    fun findTransactionsByCreatedAtAfterAndCreatedAtBefore( after:Date, before:Date):List<Transaction>
}