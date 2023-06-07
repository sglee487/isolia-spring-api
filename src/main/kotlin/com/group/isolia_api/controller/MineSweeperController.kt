package com.group.isolia_api.controller

import com.group.isolia_api.repository.minesweeper.ActionHistory
import com.group.isolia_api.repository.minesweeper.MineSweeperRepository
import com.group.isolia_api.repository.minesweeper.MineSweeperRepositoryResponse
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import org.springframework.context.event.EventListener
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.socket.messaging.SessionConnectedEvent
import org.springframework.web.socket.messaging.SessionDisconnectEvent
import java.io.File
import java.security.Principal
import java.util.*


@Serializable
data class MineSweeperPlayer(
    val sid: String,
    val name: String,
    val color: String,
)

@Serializable
enum class ActionType(
    val value: String,
) {
    @SerialName("reveal")
    REVEAL("reveal"),

    @SerialName("flag")
    FLAG("flag");
}

@Serializable
data class MineAction(
    val sid: String,
    val actionType: ActionType,
    val x: Int,
    val y: Int,
)

@Serializable
class ActionResponse(
    val action: ActionType,
    val x: Int,
    val y: Int,
    val color: String,
    val history: List<ActionHistory>,
)

@RestController
@CrossOrigin(origins = ["*"])
class MineSweeperController(
    val mineSweeperRepository: MineSweeperRepository,
    val simpleMessagingTemplate: SimpMessagingTemplate,
) {
    val players = mutableMapOf<String, MineSweeperPlayer>()
    val randomNames = File("src/main/resources/random_names.txt").readLines()

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
        val gameStatus: MineSweeperRepositoryResponse = mineSweeperRepository.getGameStatus()
        println(gameStatus)

        simpleMessagingTemplate.convertAndSend("/subscribe-mine/players", Json.encodeToString(players.values))
        simpleMessagingTemplate.convertAndSend(
            "/subscribe-mine/user/${principal.name}/start",
            Json.encodeToString(gameStatus)
        )
    }

    @MessageMapping("/restart")
    fun reset() {
        mineSweeperRepository.reset()
        simpleMessagingTemplate.convertAndSend(
            "/subscribe-mine/restart", Json.encodeToString(
                MineSweeperRepositoryResponse(
                    mineSweeperRepository.getMineField(),
                    mineSweeperRepository.getHistory(),
                )
            )
        )
    }

    @MessageMapping("/start")
    fun start(
        principal: Principal
    ) {
        val gameStatus: MineSweeperRepositoryResponse = mineSweeperRepository.getGameStatus()
        println(gameStatus)

        simpleMessagingTemplate.convertAndSend(
            "/subscribe-mine/user/${principal.name}/start",
            Json.encodeToString(gameStatus)
        )
    }

    @MessageMapping("/action")
    fun action(
        principal: Principal,
        @Payload message: String,
    ) {
        val mineAction = Json.decodeFromString<MineAction>(message)
        val name = players[mineAction.sid]?.name ?: return
        val color = players[mineAction.sid]?.color ?: return
        mineSweeperRepository.addHistory(mineAction.actionType, name, color, mineAction.x, mineAction.y)

        println(mineSweeperRepository.getHistory())
        simpleMessagingTemplate.convertAndSend(
            "/subscribe-mine/action", Json.encodeToString(
                ActionResponse(
                    action = mineAction.actionType,
                    x = mineAction.x,
                    y = mineAction.y,
                    color = color,
                    history = mineSweeperRepository.getHistory(),
                )
            )
        )

//        val gameStatus: MineSweeperRepositoryResponse = mineSweeperRepository.getGameStatus()
//        println(gameStatus)
//
//        simpleMessagingTemplate.convertAndSend(
//            "/subscribe-mine/user/${principal.name}/start",
//            Json.encodeToString(gameStatus)
//        )
    }

    @MessageMapping("/message")
    fun handleMessage(
        principal: Principal,
        accessor: SimpMessageHeaderAccessor,
        @Payload message: String,
        @Header("simpSessionId") sessionId: String
    ) {
        simpleMessagingTemplate.convertAndSend("/subscribe/user/${principal.name}/queue/reply", "dsfdsfdsf")
    }

    private fun generateNickname(): String {
        return randomNames[(Math.random() * randomNames.size).toInt()]
    }

    private fun generateColor(): String {
        return "#${(0..5).joinToString("") { Integer.toHexString((Math.random() * 16).toInt()) }}"
    }
}
