package com.miladheydari.farazpardazan.controllers

import com.miladheydari.farazpardazan.models.User
import com.miladheydari.farazpardazan.repositories.TokenRepository
import com.miladheydari.farazpardazan.repositories.UserRepository
import com.miladheydari.farazpardazan.client.response.Result
import com.miladheydari.farazpardazan.errors.ConstantErrorCodes
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity


open class BaseController(private val tokenRepository: TokenRepository, private val userRepository: UserRepository) {
    fun <T> doing(job: () -> Result<T>): ResponseEntity<Result<T>> {
        return try {
            ResponseEntity.status(HttpStatus.OK).body(job())
        } catch (t: Throwable) {

            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Result(message = t.message))
        }
    }

    private fun <T, R> doing(value: R, job: (R) -> Result<T>): ResponseEntity<Result<T>> {
        return try {
            ResponseEntity.status(HttpStatus.OK).body(job(value))
        } catch (t: Throwable) {

            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Result(message = t.message))
        }
    }

    fun <T> authThenDoing(token: String, userId: Long, job: (User) -> Result<T>): ResponseEntity<Result<T>> {
        return userRepository.findById(userId).map {

            val dbToken = tokenRepository.findTokensByActiveIsTrueAndUserId(it.id).firstOrNull { it.token == token }
            if (dbToken != null) {
                if (dbToken.active)
                    return@map doing(it, job)
                else return@map ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Result<T>(message = ConstantErrorCodes.TOKEN_EXPIRED))
            } else {
                return@map ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Result<T>(message = ConstantErrorCodes.TOKEN_NOT_VALID))
            }

        }.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Result(message = ConstantErrorCodes.USER_NOT_FOUND)))
    }

}