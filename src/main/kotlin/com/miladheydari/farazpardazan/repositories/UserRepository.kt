package com.miladheydari.farazpardazan.repositories

import com.miladheydari.farazpardazan.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findUserByPhoneNumber(phoneNumber: String): Optional<User>

}