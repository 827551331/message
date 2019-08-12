package com.hd.message.dto;

import com.hd.message.entity.MessageRecord;
import com.hd.message.entity.MessageSendRecord;
import lombok.Data;

import java.util.List;

@Data
public class MessageQueryDTO {

    private MessageRecord messageRecord;
    private List<MessageSendRecord> messageSendRecord;
}
