package com.group.isolia_api.controller

import com.group.isolia_api.schemas.user.request.*
import com.group.isolia_api.schemas.user.response.UserCreateResponse
import com.group.isolia_api.schemas.user.response.UserLoginResponse
import com.group.isolia_api.schemas.user.response.UserUpdateResponse
import com.group.isolia_api.service.user.UserService
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.view.RedirectView
import java.util.*


@RestController
@CrossOrigin(origins = ["*"])
class UserController(
    private val userService: UserService,
    @Value("\${spring.env.jwt-secret-key}")
    private val jwtSecret: String,
    @Value("\${spring.env.auth-callback-url}")
    private val authCallbackUrl: String,
) {

    private val jwtManager: JWTManager = JWTManager(jwtSecret)

    @ResponseBody
    @PostMapping("/user")
    fun registerUser(@RequestBody request: UserCreateRequest): ResponseEntity<UserCreateResponse> {
        return try {
            val user = userService.registerUser(request)

            val userSub = user.getUserSub()
            val encodedUserSub = Json.encodeToString(userSub)
            val exp: Long = 60 * 8
            val jwt = jwtManager.generateJwtToken(encodedUserSub, minutes = exp)

            ResponseEntity(UserCreateResponse(user, jwt, _exp = exp), HttpStatus.CREATED)
        } catch (e: Error) {
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

            val userSub = user.getUserSub()
            val encodedUserSub = Json.encodeToString(userSub)
            val exp: Long = 60 * 8
            val jwt = jwtManager.generateJwtToken(encodedUserSub, minutes = exp)
            ResponseEntity(UserLoginResponse(user, jwt, _exp = exp), HttpStatus.OK)
        } catch (e: IllegalArgumentException) {
            ResponseEntity(e.message, HttpStatus.UNAUTHORIZED)
        }
    }

    @GetMapping("/socialLogin/{registrationId}")
    fun socialLogin(
        @PathVariable registrationId: String
    ): RedirectView {
        println(registrationId)
        val redirectView = RedirectView()
        redirectView.url = ""
        return redirectView
    }

    @RequestMapping(
        value = ["/login/oauth2/code/{registrationId}"],
        method = [RequestMethod.GET],
        produces = ["application/json"],
    )
    fun googleLogin(
        @RequestParam code: String,
        @PathVariable registrationId: String
    ): ModelAndView {
        val user = userService.socialLogin(code, registrationId)
        val userSub = user.getUserSub()
        val encodedUserSub = Json.encodeToString(userSub)
        val exp: Long = 60 * 8
        val token = jwtManager.generateJwtToken(encodedUserSub, minutes = exp)

        val modelAndView = ModelAndView()
        modelAndView.viewName = "redirect:$authCallbackUrl"
        modelAndView.addObject("userLoginResponse", UserLoginResponse(user, token, _exp = exp).encodedToJSON())
        return modelAndView
    }
}