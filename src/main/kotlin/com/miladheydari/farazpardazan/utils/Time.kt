package com.miladheydari.farazpardazan.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

object Time {
    const val TEN_MINUTE: Long = 10 * 60 * 1000
    fun getDate(millisecondsToAdd: Long) = Date(System.currentTimeMillis() + millisecondsToAdd)
    fun generateToken(any: Any): String {
        val algorithm = Algorithm.HMAC256("secret")
        return JWT.create()
                .withIssuer(System.currentTimeMillis().toString() + "~``" + any.toString())
                .sign(algorithm)
    }
}