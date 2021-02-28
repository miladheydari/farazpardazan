package com.miladheydari.farazpardazan.controllers

import com.miladheydari.farazpardazan.client.response.Result
import com.miladheydari.farazpardazan.errors.ConstantErrorCodes
import com.miladheydari.farazpardazan.errors.InternalError
import com.miladheydari.farazpardazan.models.Sms
import com.miladheydari.farazpardazan.models.Token
import com.miladheydari.farazpardazan.models.User
import com.miladheydari.farazpardazan.repositories.SmsRepository
import com.miladheydari.farazpardazan.repositories.TokenRepository
import com.miladheydari.farazpardazan.repositories.UserRepository
import com.miladheydari.farazpardazan.utils.Time
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/auth")
class AuthController @Autowired constructor(val userRepository: UserRepository, val tokenRepository: TokenRepository)
    : BaseController(tokenRepository, userRepository) {

    @Autowired
    lateinit var smsRepository: SmsRepository


    @PostMapping("/signin")
    fun signin(@RequestBody @Valid user: User): ResponseEntity<Result<Sms>> {


        return doing {
            return@doing userRepository.findUserByPhoneNumber(user.phoneNumber).orElseGet {
                userRepository.save(user)
            }.let {
                val sms = Sms(0, it, System.currentTimeMillis().toString())

                Result(smsRepository.save(sms))
            }
        }
    }

    @PostMapping("/{userId}/sendcode")
    fun sendCode(@PathVariable("userId") userId: Long, @RequestParam("code") code: String): ResponseEntity<Result<Token>> {

        return doing {
            return@doing userRepository.findById(userId).map { user ->
                smsRepository.findByActiveIsTrueAndUserId(user.id).filter { it.active && it.code == code && it.createdAt.after(Time.getDate(-Time.TEN_MINUTE)) }
                        .map {
                            it.active = false
                            smsRepository.save(it)

                            val token = Token(0, user, Time.generateToken(user))
                            Result(tokenRepository.save(token))
                        }.ifEmpty {
                            throw InternalError(ConstantErrorCodes.ACTIVATION_CODE_INVALID)
                        }.first()
            }.orElseThrow { throw InternalError(ConstantErrorCodes.USER_NOT_FOUND) }
        }
    }


    @DeleteMapping("/{userId}/signout")
    fun signOut(@PathVariable("userId") userId: Long, @RequestHeader("token") token: String): ResponseEntity<Result<Any>> {
        return authThenDoing(token, userId) {
            val token2 = tokenRepository.findTokenByTokenAndUserId(token, userId).get()
            token2.active = false
            tokenRepository.save(token2)
            return@authThenDoing Result<Any>()
        }

    }

}