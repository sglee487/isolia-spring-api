package com.group.isolia_api.controller

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*


class JWTManager(private val jwtSecret: String) {

    fun generateJwtToken(jwtSub: String): String {
        print(jwtSecret)
        val key = Keys.hmacShaKeyFor(jwtSecret.toByteArray(StandardCharsets.UTF_8))
        return Jwts.builder()
            .setSubject(jwtSub)
            .setIssuedAt(Date.from(LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul")).toInstant()))
            .setExpiration(Date.from(LocalDateTime.now().plusHours(2).atZone(ZoneId.of("Asia/Seoul")).toInstant()))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
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