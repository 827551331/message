package com.hd.message.controller;

import com.hd.message.dto.MessageDTO;
import com.hd.message.dto.MessageType;
import com.hd.message.init.MessageQueue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/message")
public class MessageController {

    @GetMapping("/sendMsg")
    public void sendMsg() {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setSourceId("1");
        messageDTO.setTitle("测试消息");
        messageDTO.setTargetUsers(new String[]{"15997431850", "13697346443"});
        messageDTO.setMessageType(MessageType.SMSMSG);
        messageDTO.setContent("您的验证码是：2500。请不要把验证码泄露给其他人。");
        messageDTO.setLink("https://www.baidu.com/");
        MessageQueue.produce(messageDTO);
    }
}
