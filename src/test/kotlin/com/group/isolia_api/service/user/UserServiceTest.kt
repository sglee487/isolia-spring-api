package com.group.isolia_api.service.user

import com.group.isolia_api.controller.UserController
import com.group.isolia_api.domain.LoginType
import com.group.isolia_api.repository.user.UserRepository
import com.group.isolia_api.schemas.user.request.*
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@SpringBootTest
class UserServiceTest @Autowired constructor(
    private val userController: UserController,
    private val userRepository: UserRepository,
    private val userService: UserService
) {
    private val encoder = BCryptPasswordEncoder()

    @AfterEach
    fun clean() {
        userRepository.deleteAll()
    }

    @Test
    @DisplayName("유저 저장이 정상 동작한다")
    fun registerUserTest() {
        // given
        val request = UserCreateRequest(
            snsSub = null,
            loginType = LoginType.EMAIL,
            email = "sglee487@naver.com",
            password = "1234",
            displayName = "익명1",
            picture32 = null,
            picture96 = null,
        )

        // when
        val response = userController.registerUser(request)

        // then
        assertThat(response.statusCode.value()).isEqualTo(201)
        assertThat(response.body?.loginType).isEqualTo(LoginType.EMAIL)
        assertThat(response.body?.email).isEqualTo("sglee487@naver.com")
        assertThat(response.body?.displayName).isEqualTo("익명1")
        assertThat(response.body?.picture32).isEqualTo(null)
        assertThat(response.body?.picture96).isEqualTo(null)

        val results = userRepository.findAll()
        assertThat(results).hasSize(1)
        assertThat(results[0].snsSub).isEqualTo(null)
        assertThat(results[0].loginType).isEqualTo(LoginType.EMAIL)
        assertThat(results[0].email).isEqualTo("sglee487@naver.com")
        assert(encoder.matches("1234", results[0].password))
        assertThat(results[0].displayName).isEqualTo("익명1")
        assertThat(results[0].picture32).isEqualTo(null)
        assertThat(results[0].picture96).isEqualTo(null)
    }

    @Test
    @DisplayName("유저 수정이 정상 작동한다.")
    fun updateUserTest() {
        // given
        val createRequest = UserCreateRequest(
            snsSub = null,
            loginType = LoginType.EMAIL,
            email = "sglee487@naver.com",
            password = "1234",
            displayName = "익명1",
            picture32 = null,
            picture96 = null,
        )
        userService.registerUser(createRequest)

        // when
        val updateRequest = UserUpdateRequest(
            loginType = LoginType.EMAIL,
            email = "sglee487@naver.com",
            password = "1234",
            newPassword = null,
            displayName = "익명2",
            picture32 = null,
            picture96 = null,
        )
        val updateResponse = userController.updateUser(updateRequest)

        // then
        assertThat(updateResponse.statusCode.value()).isEqualTo(202)
        assertThat(updateResponse.body?.loginType).isEqualTo(LoginType.EMAIL)
        assertThat(updateResponse.body?.email).isEqualTo("sglee487@naver.com")
        assertThat(updateResponse.body?.displayName).isEqualTo("익명2")
        assertThat(updateResponse.body?.picture32).isEqualTo(null)
        assertThat(updateResponse.body?.picture96).isEqualTo(null)

        val results = userRepository.findAll()
        assertThat(results).hasSize(1)
        assertThat(results[0].snsSub).isEqualTo(null)
        assertThat(results[0].loginType).isEqualTo(LoginType.EMAIL)
        assertThat(results[0].email).isEqualTo("sglee487@naver.com")
        assert(encoder.matches("1234", results[0].password))
        assertThat(results[0].displayName).isEqualTo("익명2")
        assertThat(results[0].picture32).isEqualTo(null)
        assertThat(results[0].picture96).isEqualTo(null)
    }

    @Test
    @DisplayName("유저 로그인이 정상 작동한다.")
    fun loginUserTest() {
        // given
        val createRequest = UserCreateRequest(
            snsSub = null,
            loginType = LoginType.EMAIL,
            email = "sglee487@naver.com",
            password = "1234",
            displayName = "익명1",
            picture32 = null,
            picture96 = null,
        )
        userService.registerUser(createRequest)

        // when
        val loginRequest = UserLoginRequest(
            loginType = LoginType.EMAIL,
            email = "sglee487@naver.com",
            password = "1234",
        )
        val user = userService.loginUser(loginRequest)
        val loginResponse = userController.loginUser(loginRequest) as ResponseEntity<UserLoginResponse>

        // then
        assertThat(user).isNotNull
        assertThat(user.snsSub).isEqualTo(null)
        assertThat(user.loginType).isEqualTo(LoginType.EMAIL)
        assertThat(user.email).isEqualTo("sglee487@naver.com")
        assert(encoder.matches("1234", user.password))
        assertThat(user.displayName).isEqualTo("익명1")
        assertThat(user.picture32).isEqualTo(null)
        assertThat(user.picture96).isEqualTo(null)

        assertThat(loginResponse.statusCode.value()).isEqualTo(200)
        assertThat(loginResponse.body?.loginType).isEqualTo(LoginType.EMAIL)
        assertThat(loginResponse.body?.email).isEqualTo("sglee487@naver.com")
        assertThat(loginResponse.body?.displayName).isEqualTo("익명1")
        assertThat(loginResponse.body?.picture32).isEqualTo(null)
        assertThat(loginResponse.body?.picture96).isEqualTo(null)
    }
}