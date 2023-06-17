package com.group.isolia_api.service.board

import com.group.isolia_api.domain.BoardType
import com.group.isolia_api.domain.LoginType
import com.group.isolia_api.domain.User
import com.group.isolia_api.domain.UserSub
import com.group.isolia_api.repository.board.BoardRepository
import com.group.isolia_api.repository.comment.CommentRepository
import com.group.isolia_api.repository.user.UserRepository
import com.group.isolia_api.schemas.board.request.BoardPostCreateRequest
import com.group.isolia_api.schemas.board.response.BoardUserInfo
import com.group.isolia_api.schemas.comment.request.CommentCreateRequest
import com.group.isolia_api.schemas.user.request.UserCreateRequest
import com.group.isolia_api.service.user.UserService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("dev")
class BoardServiceTest @Autowired constructor(
    private val boardRepository: BoardRepository,
    private val boardService: BoardService,
    private val userService: UserService,
    private val userRepository: UserRepository,
    private val commentRepository: CommentRepository
) {
    private lateinit var user: User
    private lateinit var userSub: UserSub

    @BeforeEach
    fun setup() {
        user = userService.registerUser(
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
        userSub = user.getUserSub()
    }

    @AfterEach
    fun clear() {
        boardRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    @DisplayName("게시글 저장이 정상 동작한다")
    fun createPostTest() {

        // when
        val boardPostCreateRequest = BoardPostCreateRequest(
            boardType = BoardType.NOTICE,
            title = "이것은 제목",
            content = "이것은 내용"
        )
        boardService.createPost(boardPostCreateRequest, userSub)

        // then
        val boardPost = boardRepository.findAll().first()
        assertThat(boardPost.boardType).isEqualTo(BoardType.NOTICE)
        assertThat(boardPost.title).isEqualTo("이것은 제목")
        assertThat(boardPost.content).isEqualTo("이것은 내용")
        assertThat(boardPost.user).isEqualTo(user)
    }

    @Test
    @DisplayName("게시판 (게시글들) 조회가 정상 동작한다")
    fun getBoardListTest() {

        // when
        val noticeLength = 3
        val freeLength = 5

        for (i in 0 until noticeLength) {
            boardService.createPost(
                BoardPostCreateRequest(
                    boardType = BoardType.NOTICE,
                    title = "이것은 공지_${i}",
                    content = "이것은 공지 내용_${i}"
                ), userSub
            )
        }
        for (i in 0 until freeLength) {
            boardService.createPost(
                BoardPostCreateRequest(
                    boardType = BoardType.FREE,
                    title = "이것은 자유_${i}",
                    content = "이것은 자유 내용_${i}"
                ), userSub
            )
        }

        // then
        val boardListPage = boardService.getBoardList(BoardType.ALL, 1)
        assertThat(boardListPage.content.size).isEqualTo(noticeLength + freeLength)
        val noticeListPage = boardService.getBoardList(BoardType.NOTICE, 1)
        println(noticeListPage)
        assertThat(noticeListPage.content.size).isEqualTo(noticeLength)
        val freeListPage = boardService.getBoardList(BoardType.FREE, 1)
        assertThat(freeListPage.content.size).isEqualTo(freeLength)

        for (i in 0 until noticeLength) {
            assertThat(noticeListPage.content[i].title).isEqualTo("이것은 공지_${noticeLength - 1 - i}")
            assertThat(noticeListPage.content[i].boardUserInfo).isEqualTo(BoardUserInfo.of(user))
        }
        for (i in 0 until freeLength) {
            assertThat(freeListPage.content[i].title).isEqualTo("이것은 자유_${freeLength - 1 - i}")
            assertThat(freeListPage.content[i].boardUserInfo).isEqualTo(BoardUserInfo.of(user))
        }

        val results = boardRepository.findAll()
        assertThat(results.size).isEqualTo(noticeLength + freeLength)
    }

    @Test
    @DisplayName("게시글 조회가 정상 동작한다")
    fun getPostTest() {

        // when
        val boardId = boardService.createPost(
            BoardPostCreateRequest(
                boardType = BoardType.NOTICE,
                title = "이것은 공지",
                content = "이것은 공지 내용"
            ), userSub
        )!!

        // then
        val board = boardService.getBoard(boardId)!!
        assertThat(board.title).isEqualTo("이것은 공지")
        assertThat(board.content).isEqualTo("이것은 공지 내용")
        assertThat(board.userInfo).isEqualTo(BoardUserInfo.of(user))

        val results = boardRepository.findAll()
        assertThat(results.size).isEqualTo(1)
    }

    @Test
    @DisplayName("댓글 저장이 정상 동작한다")
    fun createCommentTest() {
        // given
        val boardId = boardService.createPost(
            BoardPostCreateRequest(
                boardType = BoardType.NOTICE,
                title = "이것은 공지",
                content = "이것은 공지 내용"
            ), userSub
        )!!

        // when
        for (i in 0 until 3) {
            boardService.createComment(
                CommentCreateRequest(
                    content = "이것은 댓글 내용_${i}"
                ), boardId, userSub
            )
        }

        // then
        val board = boardService.getBoard(boardId)!!
        assertThat(board.comments.size).isEqualTo(3)
        for (i in 0 until 3) {
            assertThat(board.comments[i].content).isEqualTo("이것은 댓글 내용_${i}")
            assertThat(board.comments[i].userInfo).isEqualTo(BoardUserInfo.of(user))
        }

        val results = boardRepository.findAll()
        assertThat(results.size).isEqualTo(1)

        val commentResults = commentRepository.findAll()
        assertThat(commentResults.size).isEqualTo(3)
    }
}