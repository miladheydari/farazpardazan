package com.miladheydari.farazpardazan.repositories

import com.miladheydari.farazpardazan.models.Token
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TokenRepository : JpaRepository<Token, Long> {


    fun findTokensByActiveIsTrueAndUserId(userId: Long): List<Token>
    fun findTokenByTokenAndUserId(token: String, userId: Long): Optional<Token>


}