package com.group.isolia_api.config

import org.springframework.context.annotation.Configuration
import org.springframework.http.server.ServerHttpRequest
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer
import org.springframework.web.socket.server.support.DefaultHandshakeHandler
import java.security.Principal
import java.util.*

@Configuration
@EnableWebSocketMessageBroker
@CrossOrigin(origins = ["http://localhost:5173"])
class MineSweeperWebSocketConfig : WebSocketMessageBrokerConfigurer {

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.enableSimpleBroker("/subscribe")
        registry.setApplicationDestinationPrefixes("/publish")
        registry.setUserDestinationPrefix("/user") // 없어도 기본값으로 들어있다.
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/ws-connect").setAllowedOriginPatterns("*").setHandshakeHandler(CustomHandshakeHandler).withSockJS()
    }
}

class StompPrincipal(private val name: String): Principal {
    override fun getName(): String {
        return name
    }
}

object CustomHandshakeHandler: DefaultHandshakeHandler() {
    override fun determineUser(
        request: ServerHttpRequest,
        wsHandler: WebSocketHandler,
        attributes: MutableMap<String, Any>
    ): Principal {
        return StompPrincipal(UUID.randomUUID().toString())
    }
}