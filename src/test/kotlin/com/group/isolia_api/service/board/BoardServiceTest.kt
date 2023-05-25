package com.group.isolia_api.service.board

import com.group.isolia_api.controller.BoardController
import com.group.isolia_api.repository.board.BoardRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("dev")
class BoardServiceTest @Autowired constructor(
    private val boardController: BoardController,
    private val boardRepository: BoardRepository,
    private val boardService: BoardService
) {
}