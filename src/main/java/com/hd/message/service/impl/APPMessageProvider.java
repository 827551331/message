package com.hd.message.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hd.message.dao.MessageSendRecordRepository;
import com.hd.message.dto.MessageDTO;
import com.hd.message.dto.MessageSendStatus;
import com.hd.message.dto.MessageType;
import com.hd.message.entity.MessageSendRecord;
import com.hd.message.service.MessageDelivery;
import com.hd.message.util.HttpClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Observable;
import java.util.Observer;

@Component
public class APPMessageProvider implements Observer, MessageDelivery {

    private final Logger logger = LoggerFactory.getLogger(APPMessageProvider.class);

    @Value("${integration-url}")
    private String uniwater_url;

    @Autowired
    private MessageSendRecordRepository messageSendRecordRepository;

    @Override
    public void send(MessageDTO messageDTO) {

        String[] targetUsers = messageDTO.getTargetUsers();
        MessageSendRecord messageSendRecord = null;
        for (String user : targetUsers) {

            messageSendRecord = new MessageSendRecord();

            //准备消息体
            JSONObject msg = new JSONObject();
            msg.put("access_token", messageDTO.getToken());
            msg.put("uid", user);
            msg.put("title", messageDTO.getTitle());
            msg.put("content", messageDTO.getContent());
            msg.put("alive", 0);
            JSONObject param = new JSONObject();
            param.put("type", "system");
            param.put("groupType", "system");
            msg.put("param", param);
            msg.put("action", "");
            msg.put("do", "");

            messageSendRecord.setMessageRecord_Id(messageDTO.getMessageRecord_Id());
            messageSendRecord.setMessageSendTarget(user);
            messageSendRecord.setSendTime(new Date());

            String url = uniwater_url + "/hdl/uniwater/v1.0/app/message/push.json";
            ResponseEntity<Object> responseEntity = HttpClientUtil.doPostRequest(url, msg.toJSONString());
            JSONObject result = JSONObject.parseObject(responseEntity.getBody().toString());
            if (result.getInteger("Code") == 0) {
                logger.info("APP消息推送成功，消息标题：{}", messageDTO.getTitle());
                messageSendRecord.setMessageSendStatus(MessageSendStatus.SUCCESS);
            } else {
                logger.error("APP消息推送失败，消息标题：{}，失败原因：{}", messageDTO.getTitle(), result.getString("Message"));
                messageSendRecord.setMessageSendStatus(MessageSendStatus.FAIL);
                messageSendRecord.setFailReason(result.getJSONObject("SubmitResult").getString("msg"));
            }
            messageSendRecordRepository.save(messageSendRecord);
        }

    }

    @Override
    public void update(Observable o, Object arg) {
        MessageDTO messageDTO = (MessageDTO) arg;
        //判断消息是否需要APP消息发送
        if (messageDTO.getMessageType() == MessageType.APPMSG || messageDTO.getMessageType() == MessageType.ALLMSG) {
            this.send(messageDTO);
        }
    }
}
