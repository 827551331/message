package com.hd.message.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hd.message.dao.MessageSendRecordRepository;
import com.hd.message.dto.MessageDTO;
import com.hd.message.dto.MessageSendStatus;
import com.hd.message.dto.MessageType;
import com.hd.message.entity.MessageSendRecord;
import com.hd.message.service.MessageDelivery;
import com.hd.message.util.HttpClientUtil;
import com.hd.message.util.MessageDigestUtil;
import com.hd.message.util.StaxonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

/**
 * （惠阳短信平台）短息通知服务提供者(观察者)
 *
 * @author wang_yw
 */
@Component
public class HYSMSMessageProvider implements Observer, MessageDelivery {

    private final Logger logger = LoggerFactory.getLogger(HYSMSMessageProvider.class);

    @Autowired
    private MessageSendRecordRepository messageSendRecordRepository;

    @Value("${sms-url}")
    private String smsUrl;

    @Value("${sms-appid}")
    private String smsAppid;

    @Value("${sms-secret}")
    private String smsSecret;

    @Override
    public void send(MessageDTO messageDTO) {
        logger.info("发送短信消息，标题为：{}", messageDTO.getTitle());
        logger.info("发送短信消息，内容为：{}", messageDTO.getContent());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

        MessageSendRecord messageSendRecord = null;

        String timeSpan = sdf.format(new Date()) + "6322";
        String token = MessageDigestUtil.getMD5(smsAppid + smsSecret + timeSpan);
        JSONObject smsJSON = new JSONObject();
        smsJSON.put("appId", smsAppid);
        smsJSON.put("timeSpan", timeSpan);
        smsJSON.put("token", token);
        smsJSON.put("smsContent", messageDTO.getContent());

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

            smsJSON.put("phoneList", new String[]{target});

            ResponseEntity<Object> responseEntity = HttpClientUtil.doPostRequest(smsUrl, smsJSON.toJSONString());

            //1、判断是否请求成功
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                JSONObject result = JSONObject.parseObject(responseEntity.getBody().toString());

                //2、判断是否业务处理成功
                if (result.getInteger("code") == 1) {
                    logger.info("发送成功,目标：{}.", target);
                    messageSendRecord.setMessageSendStatus(MessageSendStatus.SUCCESS);
                } else {
                    logger.info("发送失败，目标：{};原因：{}", target, result.getJSONObject("SubmitResult").getString("msg"));
                    messageSendRecord.setMessageSendStatus(MessageSendStatus.FAIL);
                    messageSendRecord.setFailReason(result.getJSONObject("SubmitResult").getString("msg"));
                }
                messageSendRecordRepository.save(messageSendRecord);
            } else {
                logger.error("发送请求失败，目标：{};原因：{}", target, responseEntity.getBody().toString());
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        MessageDTO messageDTO = (MessageDTO) arg;
        //判断消息是否需要短信发送
        if (messageDTO.getMessageType() == MessageType.SMSMSG || messageDTO.getMessageType() == MessageType.ALLMSG) {
            this.send(messageDTO);
        }
    }
}
