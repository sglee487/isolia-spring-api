package com.group.isolia_api.schemas.board.response

import com.group.isolia_api.domain.Board
import com.group.isolia_api.domain.User

class BoardGetResponse(
    val borad: Board,
    val user: User
) {

}