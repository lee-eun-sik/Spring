package back.controller.chat;


import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
public class ChatController {

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public Map<String, Object> sendMessage(Map<String, Object> message) {
        // message 안에 "sender", "content", "type" 등이 들어있다고 가정
        return message;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public Map<String, Object> addUser(Map<String, Object> message) {
        // 예: type이 "JOIN"일 때 메시지 내용 수정 가능
        message.put("content", message.get("sender") + "님이 입장하셨습니다.");
        return message;
    }
}