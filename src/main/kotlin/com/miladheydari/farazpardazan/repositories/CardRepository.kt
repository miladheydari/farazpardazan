package com.miladheydari.farazpardazan.repositories

import com.miladheydari.farazpardazan.models.Card
import com.miladheydari.farazpardazan.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CardRepository : JpaRepository<Card, Long> {
    fun findCardsByUserAndActiveIsTrue(user: User): List<Card>
    fun findCardByIdAndUserAndActiveIsTrue(id:Long,user: User): Optional<Card>
}