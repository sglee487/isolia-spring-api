package com.group.isolia_api.controller

import com.group.isolia_api.domain.BoardType
import com.group.isolia_api.domain.UserSub
import com.group.isolia_api.schemas.board.request.BoardPostCreateRequest
import com.group.isolia_api.schemas.board.response.BoardGetResponse
import com.group.isolia_api.schemas.board.response.BoardPostResponse
import com.group.isolia_api.schemas.comment.request.CommentCreateRequest
import com.group.isolia_api.service.board.BoardService
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.security.SignatureException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.*

@RestController
class BoardController(
    val boardService: BoardService,
    @Value("\${spring.env.jwt-secret-key}")
    private val jwtSecret: String = "default"
) {

    private val jwtManager: JWTManager = JWTManager(jwtSecret)

    @ResponseBody
    @PostMapping("/board")
    fun createPost(
        @RequestHeader("Authorization") authorization: String,
        @RequestBody requestBody: BoardPostCreateRequest
    ): Long {
        try {
            val token = authorization.substringAfter("Bearer ")

            // get sub from decoded token
            val userSub = Json.decodeFromString<UserSub>(jwtManager.decodeJWT(token))

            return boardService.createPost(requestBody, userSub)!!
        } catch (e: MalformedJwtException) {
            // 토큰 형식이 유효하지 않음
            println("jwt malformed")
            return 0
        } catch (e: ExpiredJwtException) {
            // 토큰 만료
            println("jwt expired")
            return 0
        } catch (e: SignatureException) {
            // 서명(키) 불일치
            println("jwt signature error")
            return 0
        } catch (e: IllegalArgumentException) {
            // 토큰이 비어있거나, null
            println("jwt is empty")
            return 0
        } catch (e: Exception) {
            // 기타
            println("error")
            return 0
        }

    }

    @ResponseBody
    @PostMapping("/comment/{boardId}")
    fun createPost(
        @RequestHeader("Authorization") authorization: String,
        @PathVariable("boardId") boardId: Long,
        @RequestBody requestBody: CommentCreateRequest
    ): Long {
        try {
            val token = authorization.substringAfter("Bearer ")

            val userSub = Json.decodeFromString<UserSub>(jwtManager.decodeJWT(token))

            return boardService.createComment(requestBody, boardId, userSub)
        } catch (e: MalformedJwtException) {
            // 토큰 형식이 유효하지 않음
            println("jwt malformed")
            return 0
        } catch (e: ExpiredJwtException) {
            // 토큰 만료
            println("jwt expired")
            return 0
        } catch (e: SignatureException) {
            // 서명(키) 불일치
            println("jwt signature error")
            return 0
        } catch (e: IllegalArgumentException) {
            // 토큰이 비어있거나, null
            println("jwt is empty")
            return 0
        }

    }

    @GetMapping("/board")
    fun getPosts(@RequestParam("boardType") boardType: BoardType?): List<BoardGetResponse> {
        return boardService.getBoardList(boardType)
    }

    @GetMapping("/post/", "/post/{id}")
    fun getPost(@PathVariable("id") id: Long?): BoardPostResponse? = id?.let {
        boardService.getBoard(it)
    }


}