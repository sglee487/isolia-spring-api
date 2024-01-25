package com.group.isolia_api.controller

import com.group.isolia_api.domain.MineSweeper.MineAction
import com.group.isolia_api.domain.MineSweeper.MineSweeperPlayer
import com.group.isolia_api.repository.minesweeper.MineSweeperRepository
import com.group.isolia_api.schemas.MineSweeper.response.ActionResponse
import com.group.isolia_api.schemas.MineSweeper.response.MineSweeperRepositoryResponse
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import org.springframework.context.event.EventListener
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.socket.messaging.SessionConnectedEvent
import org.springframework.web.socket.messaging.SessionDisconnectEvent
import java.io.File
import java.security.Principal
import java.util.*

@RestController
@CrossOrigin(origins = [
    "http://localhost:5173", 
    "https://isolia.shop",
    "http://isolia.xyz",
    "https://isolia.xyz",
    ])
class MineSweeperController(
    val mineSweeperRepository: MineSweeperRepository,
    val simpleMessagingTemplate: SimpMessagingTemplate,
) {
    val players = mutableMapOf<String, MineSweeperPlayer>()
    val randomNames = File("src/main/resources/random_names.txt").readLines()

    private fun generateNickname(): String {
        return randomNames[(Math.random() * randomNames.size).toInt()]
    }

    private fun generateColor(): String {
        return "#${(0..5).joinToString("") { Integer.toHexString((Math.random() * 16).toInt()) }}"
    }

    fun ActionResponse.encodeToString() = Json.encodeToString(this)
    fun MineSweeperRepositoryResponse.encodeToString() = Json.encodeToString(this)

    @EventListener
    fun onSessionConnected(event: SessionConnectedEvent) {
        val principal = event.user ?: return
        players[principal.name] = MineSweeperPlayer(
            sid = principal.name,
            name = generateNickname(),
            color = generateColor(),
        )
    }

    @EventListener
    fun onSessionDisconnect(event: SessionDisconnectEvent) {
        val principal = event.user ?: return
        players.remove(principal.name)
        simpleMessagingTemplate.convertAndSend("/subscribe-mine/players", Json.encodeToString(players.values))
    }

    @MessageMapping("/join")
    fun join(
        principal: Principal,
    ) {
        simpleMessagingTemplate.convertAndSend("/subscribe-mine/players", Json.encodeToString(players.values))
        simpleMessagingTemplate.convertAndSend(
            "/subscribe-mine/user/${principal.name}/start", mineSweeperRepository.getGameStatus().encodeToString()
        )
    }

    @MessageMapping("/restart")
    fun reset() {
        mineSweeperRepository.reset()
        simpleMessagingTemplate.convertAndSend(
            "/subscribe-mine/restart", mineSweeperRepository.getGameStatus().encodeToString()
        )
    }

    @MessageMapping("/start")
    fun start(
        principal: Principal
    ) {
        val gameStatus: MineSweeperRepositoryResponse = mineSweeperRepository.getGameStatus()

        simpleMessagingTemplate.convertAndSend(
            "/subscribe-mine/user/${principal.name}/start", Json.encodeToString(gameStatus)
        )
    }

    @MessageMapping("/action")
    fun action(
        principal: Principal,
        @Payload mineAction: MineAction,
    ) {
        println(mineAction)
        val name = players[mineAction.sid]?.name ?: return
        val color = players[mineAction.sid]?.color ?: return
        mineSweeperRepository.addHistory(mineAction.actionType, name, color, mineAction.x, mineAction.y)

        simpleMessagingTemplate.convertAndSend(
            "/subscribe-mine/action",
            ActionResponse(
                action = mineAction.actionType,
                x = mineAction.x,
                y = mineAction.y,
                color = color,
                history = mineSweeperRepository.getHistory(),
            ).encodeToString()
        )
    }
}
