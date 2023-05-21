package com.group.isolia_api.service.board

import com.group.isolia_api.domain.Board
import com.group.isolia_api.domain.UserSub
import com.group.isolia_api.repository.board.BoardRepository
import com.group.isolia_api.repository.user.UserRepository
import com.group.isolia_api.schemas.board.request.BoardPostCreateRequest
import org.springframework.stereotype.Service

@Service
class BoardService(
    private val boardRepository: BoardRepository,
    private val userRepository: UserRepository
) {

    fun createPost(request: BoardPostCreateRequest, userSub: UserSub): Long? {
        val user = userRepository.getReferenceById(userSub.id)
        val board = Board(
            boardType = request.boardType,
            title = request.title,
            content = request.content,
            previewText = "이것은 프리뷰",
            previewImage = null,
            user = user
        )
        return boardRepository.save(board).id
    }

}