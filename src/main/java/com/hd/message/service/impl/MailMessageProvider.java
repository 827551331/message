package com.hd.message.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hd.message.dao.MessageSendRecordRepository;
import com.hd.message.dto.MessageDTO;
import com.hd.message.dto.MessageSendStatus;
import com.hd.message.dto.MessageType;
import com.hd.message.entity.MessageSendRecord;
import com.hd.message.service.MessageDelivery;
import com.hd.message.util.HttpClientUtil;
import com.hd.message.util.StaxonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Observable;
import java.util.Observer;

/**
 * 邮件通知服务提供者(观察者)
 *
 * @author wang_yw
 */
@Component
public class MailMessageProvider implements Observer, MessageDelivery {

    private final Logger logger = LoggerFactory.getLogger(MailMessageProvider.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MessageSendRecordRepository messageSendRecordRepository;

    @Override
    public void send(MessageDTO messageDTO) {
        logger.info("发送邮件消息，标题为：{}", messageDTO.getTitle());
        logger.info("发送邮件消息，内容为：{}", messageDTO.getContent());

        MessageSendRecord messageSendRecord = null;
        String[] targets = messageDTO.getTargetUsers();
        for (int i = 0; i < targets.length; i++) {
            messageSendRecord = new MessageSendRecord();

            String target = targets[i];
            if (StringUtils.isEmpty(target)) {
                continue;
            }
            messageSendRecord.setMessageRecord_Id(messageDTO.getMessageRecord_Id());
            messageSendRecord.setMessageSendTarget(target);
            messageSendRecord.setSendTime(new Date());


            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("827551331@qq.com");
            message.setTo(target);
            message.setSubject(messageDTO.getTitle());
            message.setText(messageDTO.getContent());
            mailSender.send(message);

            logger.info("发送成功,目标：{}.", target);
            messageSendRecord.setMessageSendStatus(MessageSendStatus.SUCCESS);
            messageSendRecordRepository.save(messageSendRecord);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        MessageDTO messageDTO = (MessageDTO) arg;
        //判断消息是否需要邮件发送
        if (messageDTO.getMessageType() == MessageType.MAILMSG || messageDTO.getMessageType() == MessageType.ALLMSG) {
            this.send(messageDTO);
        }
    }
}
