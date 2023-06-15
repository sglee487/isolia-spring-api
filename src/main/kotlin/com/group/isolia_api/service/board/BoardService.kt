package com.group.isolia_api.service.board

import com.group.isolia_api.domain.Board
import com.group.isolia_api.domain.BoardType
import com.group.isolia_api.domain.Comment
import com.group.isolia_api.domain.UserSub
import com.group.isolia_api.repository.board.BoardQuerydslRepository
import com.group.isolia_api.repository.board.BoardRepository
import com.group.isolia_api.repository.comment.CommentRepository
import com.group.isolia_api.repository.user.UserRepository
import com.group.isolia_api.schemas.board.request.BoardPostCreateRequest
import com.group.isolia_api.schemas.board.response.BoardGetResponse
import com.group.isolia_api.schemas.board.response.BoardPostResponse
import com.group.isolia_api.schemas.comment.request.CommentCreateRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BoardService(
    private val boardRepository: BoardRepository,
    private val boardQuerydslRepository: BoardQuerydslRepository,
    private val userRepository: UserRepository,
    private val commentRepository: CommentRepository
) {
    @Transactional
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

    @Transactional
    fun createComment(request: CommentCreateRequest, boardId: Long, userSub: UserSub): Long {
        val user = userRepository.getReferenceById(userSub.id)
        val board = boardRepository.getReferenceById(boardId)

        val comment = Comment(
            content = request.content,
            user = user,
            board = board
        )
        commentRepository.save(comment)
        board.comments.add(comment)
        return board.id!!
    }

    @Transactional
    fun getBoardList(boardType: BoardType? = null): List<BoardGetResponse> =
        boardQuerydslRepository.getBoardList(boardType).map { board ->
            BoardGetResponse.of(board, board.user)
        }

    @Transactional
    fun getBoard(id: Long): BoardPostResponse? = boardRepository.getByIdAndActiveIsTrue(id)?.let { board ->
        board.hits++
        BoardPostResponse.of(board, board.user)
    }
}