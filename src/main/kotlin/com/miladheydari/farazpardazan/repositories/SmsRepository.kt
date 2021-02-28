package com.miladheydari.farazpardazan.repositories

import com.miladheydari.farazpardazan.models.Sms
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SmsRepository :JpaRepository<Sms,Long>{


    fun findByUserId(userId:Long) : List<Sms>


    fun findByActiveIsTrueAndUserId(userId:Long): List<Sms>

}