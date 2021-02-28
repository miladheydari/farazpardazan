package com.miladheydari.farazpardazan.services

import com.miladheydari.farazpardazan.feign.*
import com.miladheydari.farazpardazan.models.*
import com.miladheydari.farazpardazan.repositories.TransactionRepository
import com.miladheydari.farazpardazan.repositories.TransactionSmsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.*

@Service
class TransactionService @Autowired constructor(
    private val transactionRepository: TransactionRepository,
    private val firstPaymentApi: FirstPaymentApi,
    private val secondPaymentApi: SecondPaymentApi,
    private val transactionSmsRepository: TransactionSmsRepository
) {
    fun transfer(
        sourceCard: Card,
        amount: BigDecimal,
        destCardNumber: String,
        expDate: String,
        cvv2: String,
        pin: String
    ): Transaction {
        var transaction =
            Transaction(0, sourceCard, destCardNumber, TransactionState.FAIL, amount, sourceCard.user)
        try {
            val result: Boolean
            if (sourceCard.number.startsWith("6037")) {
                result = firstPaymentApi.postTransaction(
                    FirstPaymentApiPostModel(
                        transaction.sourceCardNumber.number,
                        transaction.destCardNumber, cvv2, expDate, pin, transaction.amount.toLong()
                    )
                )
            } else {
                result = secondPaymentApi.postTransaction(
                    SecondPaymentApiPostModel(
                        transaction.sourceCardNumber.number,
                        transaction.destCardNumber, cvv2, expDate, pin, transaction.amount.toLong()
                    )
                )

            }
            if (result)
                transaction.state = TransactionState.SUCCESS
        } catch (throwable: Throwable) {

        }
        transaction = transactionRepository.save(transaction)
        if (transaction.state == TransactionState.SUCCESS)
            transactionSmsRepository.save(
                TransactionSms(
                    0,
                    transaction,
                    "from ${transaction.user.phoneNumber} to ${transaction.destCardNumber} amount: ${transaction.amount.toLong()}",
                    transaction.user.phoneNumber
                )
            )

        return transaction
    }

    fun getReport(before: Date, after: Date): Map<Card, List<Transaction>> {
        val trans = transactionRepository.findTransactionsByCreatedAtAfterAndCreatedAtBefore(after, before)
        return trans.groupBy { it.sourceCardNumber }

    }

}