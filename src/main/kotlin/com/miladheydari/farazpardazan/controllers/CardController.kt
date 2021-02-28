package com.miladheydari.farazpardazan.controllers

import com.miladheydari.farazpardazan.client.response.Result
import com.miladheydari.farazpardazan.models.Card
import com.miladheydari.farazpardazan.repositories.*
import com.miladheydari.farazpardazan.services.CardService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/card")
class CardController @Autowired constructor(
    userRepository: UserRepository, tokenRepository: TokenRepository,
    private val cardService: CardService
) : BaseController(tokenRepository, userRepository) {



    @PostMapping("/{userId}/add")
    fun addCard(
        @PathVariable("userId") userId: Long, @RequestHeader("token") token: String,
        @RequestBody card: Card
    ): ResponseEntity<Result<Card>> {
        return authThenDoing(token, userId) {
            card.user = it
            cardService.addCard(card)
            Result(cardService.addCard(card))
        }
    }


    @GetMapping("/{userId}")
    fun getCards(
        @PathVariable("userId") userId: Long,
        @RequestHeader("token") token: String
    ): ResponseEntity<Result<List<Card>>> {
        return authThenDoing(token, userId) { user ->
            Result(cardService.getCards(user))
        }
    }


    @DeleteMapping("/{userId}/{cardId}")
    fun deleteCard(
        @PathVariable("userId") userId: Long,
        @PathVariable("cardId") cardId: Long,
        @RequestHeader("token") token: String
    ): ResponseEntity<Result<Long>> {
        return authThenDoing(token, userId) {

            Result(cardService.removeCard(cardId, it))
        }
    }
}