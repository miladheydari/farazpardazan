package com.miladheydari.farazpardazan.services

import com.miladheydari.farazpardazan.models.Card
import com.miladheydari.farazpardazan.models.User
import com.miladheydari.farazpardazan.repositories.CardRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CardService @Autowired constructor(
    private val cardRepository: CardRepository
) {
    fun addCard(card: Card): Card {
        return cardRepository.save(card)
    }

    fun removeCard(cardId: Long, user: User): Long {
        val card = cardRepository.findCardByIdAndUserAndActiveIsTrue(cardId, user)
        if (card.isPresent) {
            val updatedCard = card.get()
            updatedCard.active = false
            cardRepository.save(updatedCard)
            return updatedCard.id
        }
        return -1

    }

    fun getCards(user: User): List<Card> {
        return cardRepository.findCardsByUserAndActiveIsTrue(user)
    }

}