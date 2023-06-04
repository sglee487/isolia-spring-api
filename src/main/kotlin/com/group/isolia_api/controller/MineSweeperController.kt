package com.group.isolia_api.controller

import com.group.isolia_api.repository.minesweeper.MineSweeperRepository
import org.springframework.messaging.MessageHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.messaging.simp.SimpMessageType
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@CrossOrigin(origins = ["*"])
class MineSweeperController(
    val mineSweeperRepository: MineSweeperRepository,
    val simpleMessagingTemplate: SimpMessagingTemplate,
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

//    @MessageMapping("/message")
//    @SendToUser("/queue/reply")
//    fun handleMessage(
//        @Payload message: String,
//        accessor: SimpMessageHeaderAccessor
//    ): String {
//        val sessionId = accessor.sessionId
//        // Process the message and prepare the response
//        val response = "Hello, $sessionId! You sent: $message"
//        println(response)
//        return response
//    }

    private fun createHeaders(sessionId: String?): MessageHeaders {
        val headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE)
        sessionId?.let {
            headerAccessor.sessionId = sessionId
        }
        headerAccessor.setLeaveMutable(true)
        return headerAccessor.messageHeaders
    }

    @MessageMapping("/message")
    fun handleMessage(
        principal: Principal,
        accessor: SimpMessageHeaderAccessor,
        @Payload message: String,
        @Header("simpSessionId") sessionId: String
    ) {
        println(principal)
        println(principal.name)
        println(message)
        println(createHeaders(principal.name))
        println(accessor.sessionId)
        println(accessor.messageHeaders)
        println(accessor.id)
        println(sessionId)

        simpleMessagingTemplate.convertAndSend("/subscribe/queue/reply", "dsfdsfdsf")
        simpleMessagingTemplate.convertAndSend("/subscribe/${sessionId}/queue/reply", "dsfdsfdsf")
        simpleMessagingTemplate.convertAndSend("/subscribe/user/${sessionId}/queue/reply", "dsfdsfdsf")

        simpleMessagingTemplate.convertAndSend("/subscribe/${principal.name}/queue/reply", "dsfdsfdsf")
        simpleMessagingTemplate.convertAndSend("/subscribe/user/${principal.name}/queue/reply", "dsfdsfdsf")

        simpleMessagingTemplate.convertAndSendToUser(principal.name, "/queue/reply", "dsfdsfdsf")


////        simpleMessagingTemplate.convertAndSendToUser()
//        simpleMessagingTemplate.convertAndSendToUser(principal.name, "/queue/reply", "dsfdsfdsf")
//        simpleMessagingTemplate.convertAndSendToUser(principal.name, "/queue/reply", "dsfdsfdsf", createHeaders(principal.name))
//        simpleMessagingTemplate.convertAndSendToUser(accessor.sessionId!!, "/queue/reply", "dsfdsfdsf")
//        simpleMessagingTemplate.convertAndSendToUser(accessor.sessionId!!, "/queue/reply", "dsfdsfdsf", createHeaders(accessor.sessionId!!))
//
//        simpleMessagingTemplate.convertAndSendToUser(principal.name, "/reply", "dsfdsfdsf")
//        simpleMessagingTemplate.convertAndSendToUser(principal.name, "/reply", "dsfdsfdsf", createHeaders(principal.name))
//        simpleMessagingTemplate.convertAndSendToUser(accessor.sessionId!!, "/reply", "dsfdsfdsf")
//        simpleMessagingTemplate.convertAndSendToUser(accessor.sessionId!!, "/reply", "dsfdsfdsf", createHeaders(accessor.sessionId!!))



        //        val sessionId = accessor.sessionId!!
//        // Process the message and prepare the response
//        val response = "Hello, $sessionId! You sent: $message"
//        println(response)
//        simpleMessagingTemplate.convertAndSendToUser(sessionId, "/queue/reply", response, accessor.messageHeaders)
    }

}