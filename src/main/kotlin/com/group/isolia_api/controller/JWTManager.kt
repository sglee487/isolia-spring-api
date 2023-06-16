package com.group.isolia_api.controller

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*


class JWTManager(private val jwtSecret: String) {

    fun generateJwtToken(jwtSub: String, minutes: Long? = null): Pair<String, Date> {
        val key = Keys.hmacShaKeyFor(jwtSecret.toByteArray(StandardCharsets.UTF_8))
        val expiration = LocalDateTime.now().plusMinutes(minutes ?: (60 * 2))
        val expirationDate = Date.from(expiration.atZone(ZoneId.of("Asia/Seoul")).toInstant())

        return Pair(
            Jwts.builder()
                .setSubject(jwtSub)
                .setIssuedAt(Date.from(LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul")).toInstant()))
                .setExpiration(expirationDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact(), expirationDate
        )
    }


    fun decodeJWT(token: String): String {
        // get sub from decoded token
        return Jwts.parserBuilder()
            .setSigningKey(jwtSecret.toByteArray(StandardCharsets.UTF_8))
            .build()
            .parseClaimsJws(token)
            .body
            .subject
    }
}