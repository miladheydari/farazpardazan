package com.miladheydari.farazpardazan.controllers

import com.miladheydari.farazpardazan.client.response.Result
import com.miladheydari.farazpardazan.errors.ConstantErrorCodes
import com.miladheydari.farazpardazan.models.Card
import com.miladheydari.farazpardazan.models.Transaction
import com.miladheydari.farazpardazan.repositories.*
import com.miladheydari.farazpardazan.services.TransactionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.util.*


@RestController
@RequestMapping("/api/transaction")
class TransactionController @Autowired constructor(
    userRepository: UserRepository, tokenRepository: TokenRepository,
    private val transactionService: TransactionService,
    private val cardRepository: CardRepository
) : BaseController(tokenRepository, userRepository) {


    @PostMapping("/{userId}/transfer")
    fun transfer(
        @PathVariable("userId") userId: Long, @RequestHeader("token") token: String,
        @RequestParam cardId: Long, @RequestParam destCardNumber: String, @RequestParam cvv2: String,
        @RequestParam pin2: String, @RequestParam expDate: String, @RequestParam amount: BigDecimal
    ): ResponseEntity<Result<Transaction>> {
        return authThenDoing(token, userId) {
            val cardSource = cardRepository.findCardsByUserAndActiveIsTrue(it).find { card -> card.id == cardId }
            if (cardSource != null)
                return@authThenDoing Result(
                    transactionService.transfer(
                        cardSource,
                        amount,
                        destCardNumber,
                        expDate,
                        cvv2,
                        pin2
                    )
                )
            else
                throw InternalError(ConstantErrorCodes.ACTION_INVALID)
        }
    }


    @GetMapping("/{userId}")
    fun getReport(
        @PathVariable("userId") userId: Long,
        @RequestHeader("token") token: String,
        @RequestParam before: Date,
        @RequestParam after: Date,

        ): ResponseEntity<Result<Map<Card, List<Transaction>>>> {
        return authThenDoing(token, userId) { user ->
            Result(transactionService.getReport(before, after))
        }
    }

}