package com.check.user_check.controller.common;

import com.check.user_check.dto.SocketMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send")
    public void send(SocketMessage socketMessage){

        messagingTemplate.convertAndSend("/topic/public", socketMessage);

    }
}
