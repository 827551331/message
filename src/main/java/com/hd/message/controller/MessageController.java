package com.hd.message.controller;

import com.hd.message.dao.MessageRecordRepository;
import com.hd.message.dto.MessageDTO;
import com.hd.message.dto.ResponseData;
import com.hd.message.entity.MessageRecord;
import com.hd.message.init.MessageQueue;
import com.hd.message.util.CglibUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageRecordRepository messageRecordRepository;

    @PostMapping("/send")
    public ResponseData send(@RequestBody MessageDTO messageDTO) {

        if (StringUtils.isEmpty(messageDTO.getTitle())) {
            return ResponseData.getInstance("1001", "title不能为空", null);
        }
        if (StringUtils.isEmpty(messageDTO.getContent())) {
            return ResponseData.getInstance("1001", "content不能为空", null);
        }
        if (StringUtils.isEmpty(messageDTO.getMessageType())) {
            return ResponseData.getInstance("1001", "MessageType不能为空", null);
        }
        if (StringUtils.isEmpty(messageDTO.getSourceId())) {
            return ResponseData.getInstance("1001", "SourceId不能为空", null);
        }
        String[] targets = messageDTO.getTargetUsers();
        if (targets == null || targets.length == 0) {
            return ResponseData.getInstance("1001", "TargetUsers不能为空", null);
        }

        MessageRecord messageRecord = new MessageRecord();
        CglibUtil.copyObject(messageDTO, messageRecord);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < targets.length; i++) {
            sb.append(targets[i]);
            if (i != (targets.length - 1)) {
                sb.append(",");
            }
        }
        messageRecord.setTargetUsers(sb.toString());
        //记录消息
        MessageRecord record = messageRecordRepository.save(messageRecord);

        //准备发送
        messageDTO.setMessageRecord_Id(record.getId());
        MessageQueue.produce(messageDTO);

        //实时响应
        return ResponseData.getInstance("9999", "消息录入成功", null);
    }
}
