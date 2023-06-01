package com.group.isolia_api.controller

import com.group.isolia_api.repository.minesweeper.MineSweeperRepository
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin(origins = ["*"])
class MineSweeperController(
    val mineSweeperRepository: MineSweeperRepository,
    val simpleMessagingTemplate: SimpMessagingTemplate
) {

    @GetMapping("/reset")
    fun reset() {
        mineSweeperRepository.reset()
    }

    @MessageMapping("/hello")
    fun greeting(message: String){
        simpleMessagingTemplate.convertAndSend("/subscribe/mine", message)
    }

    @MessageMapping("/join")
    fun generateNickname(@Header("simpSessionId") sessionId: String) {
        println(sessionId)
        simpleMessagingTemplate.convertAndSendToUser(sessionId, "/subscribe/mine", "jourge")
    }

}