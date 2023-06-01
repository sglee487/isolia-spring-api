package com.group.isolia_api.controller

import com.group.isolia_api.repository.minesweeper.MineSweeperRepository
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin(origins = ["*"])
class MineSweeperController(
    val mineSweeperRepository: MineSweeperRepository
) {

    @GetMapping("/reset")
    fun reset() {
        mineSweeperRepository.reset()
    }

}