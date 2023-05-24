package com.group.isolia_api.controller

import com.group.isolia_api.domain.LoginType
import com.group.isolia_api.schemas.user.request.UserCreateRequest
import com.group.isolia_api.service.board.BoardService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ActiveProfiles

private val <T> ResponseEntity<T>.jwt: String?
    get() {
        return this.headers["Authorization"]?.get(0)?.substring(7)
    }

@SpringBootTest
@ActiveProfiles("dev")
class BoardControllerTests @Autowired constructor(
    private val boardService: BoardService,
    private val userController: UserController
) {
    @Value("\${spring.env.jwt-secret-key}")
    private val jwtSecret: String = "default"

    private var jwt: String? = null

    @BeforeEach
    fun createUser() {
        val responseEntity = userController.registerUser(
            UserCreateRequest(
                snsSub = null,
                loginType = LoginType.EMAIL,
                email = "sglee487@naver.com",
                password = "1234",
                displayName = "익명1",
                picture32 = null,
                picture96 = null,
            )
        )

        print(responseEntity.jwt)
    }

    @Test
    @DisplayName("게시물 저장이 정상 작동한다")
    fun createPostTest() {

    }


}