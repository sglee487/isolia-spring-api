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
import com.group.isolia_api.service.ImageUtils
import com.group.isolia_api.service.S3Service
import org.jsoup.Jsoup
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.net.URL
import javax.imageio.ImageIO

@Service
class BoardService(
    private val boardRepository: BoardRepository,
    private val boardQuerydslRepository: BoardQuerydslRepository,
    private val userRepository: UserRepository,
    private val commentRepository: CommentRepository,

    val s3Service: S3Service,
) {

    fun MultipartFile.toFile() = ImageUtils.changeImageMultipartFileToFile(this)

    private fun generateImageUrl(file: MultipartFile, uploadPath: String): URL {
        val fileName = file.originalFilename ?: throw IllegalArgumentException("Invalid file name")
        val (fileNameNoExt, fileExtension) = fileName.split(".")
        val uploadFileName = "${fileNameNoExt}_${System.currentTimeMillis()}.$fileExtension"
        val uploadFilePath = "$uploadPath/$uploadFileName"

        return s3Service.uploadToS3(uploadFilePath, file.toFile())
    }

    fun getImageFileNameFromUrl(url: URL): String {
        return File(url.path).name
    }

    private fun generatePreviewImageUrl(imageUrl: URL): URL {
        val imageName = getImageFileNameFromUrl(imageUrl)
        val uploadPath = "board/preview/${imageName}"
        val previewImage = ImageIO.read(imageUrl)
        val resizedImage = ImageUtils.resizeImage(previewImage, 256, 192)

        return s3Service.uploadImageToS3(resizedImage, uploadPath)
    }

    fun uploadPostImages(files: List<MultipartFile>, userSub: UserSub): List<URL> {
        val uploadPath = "board/${userSub.id}"

        return files.map { file ->
            generateImageUrl(file, uploadPath)
        }
    }


    @Transactional
    fun createPost(request: BoardPostCreateRequest, userSub: UserSub): Long? {
        val user = userRepository.getReferenceById(userSub.id)
        val contentText = Jsoup.parse(request.content).text()
        val firstImage = Jsoup.parse(request.content).select("img").first()?.attr("src")
        val previewImage = firstImage?.let { generatePreviewImageUrl(URL(it)) }
        val board = Board(
            boardType = request.boardType,
            title = request.title,
            content = request.content,
            previewText = contentText.take(140),
            previewImage = previewImage,
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
    fun getBoardList(boardType: BoardType, page: Int): Page<BoardGetResponse> {
        val result = boardQuerydslRepository.getBoardList(boardType, page)

        return result.map { board ->
            BoardGetResponse.of(board, board.user)
        }
    }

    @Transactional
    fun getBoard(id: Long): BoardPostResponse? = boardRepository.getByIdAndActiveIsTrue(id)?.let { board ->
        board.hits++
        BoardPostResponse.of(board, board.user)
    }
}