package com.group.isolia_api.controller

import com.group.isolia_api.schemas.user.request.*
import com.group.isolia_api.schemas.user.response.UserCreateResponse
import com.group.isolia_api.schemas.user.response.UserLoginResponse
import com.group.isolia_api.schemas.user.response.UserUpdateResponse
import com.group.isolia_api.service.user.UserService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@RestController
class UserController(
    private val userService: UserService,
) {

    @Value("\${spring.env.jwt-secret-key}")
    private val jwtSecret: String = "default"

    fun generateJwtToken(jwtSub: String): String {
        val key = Keys.hmacShaKeyFor(jwtSecret.toByteArray(StandardCharsets.UTF_8))
        return Jwts.builder()
            .setSubject(jwtSub)
            .setIssuedAt(Date.from(LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul")).toInstant()))
            .setExpiration(Date.from(LocalDateTime.now().plusHours(2).atZone(ZoneId.of("Asia/Seoul")).toInstant()))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    @ResponseBody
    @PostMapping("/user")
    fun registerUser(@RequestBody request: UserCreateRequest): ResponseEntity<UserCreateResponse> {
        return try {
            val user = userService.registerUser(request)

            val jwt = generateJwtToken(user.getJwtSub())

            ResponseEntity(UserCreateResponse(user, jwt), HttpStatus.CREATED)
        } catch (e: DataIntegrityViolationException) {
            ResponseEntity(HttpStatus.NOT_ACCEPTABLE)
        }
    }

    @ResponseBody
    @PatchMapping("/user")
    fun updateUser(@RequestBody request: UserUpdateRequest): ResponseEntity<UserUpdateResponse> {
        val user = userService.updateUser(request)
        return ResponseEntity(UserUpdateResponse(user), HttpStatus.ACCEPTED)
    }

    @ResponseBody
    @PostMapping("/user-login")
    fun loginUser(@RequestBody request: UserLoginRequest): ResponseEntity<*> {
        return try {
            val user = userService.loginUser(request)

            val jwt = generateJwtToken(user.getJwtSub())

            ResponseEntity(UserLoginResponse(user, jwt), HttpStatus.OK)
        } catch (e: IllegalArgumentException) {
            ResponseEntity(e.message, HttpStatus.UNAUTHORIZED)
        }
    }
}