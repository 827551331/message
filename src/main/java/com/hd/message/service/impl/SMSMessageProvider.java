package com.hd.message.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hd.message.dao.MessageRepository;
import com.hd.message.dto.MessageDTO;
import com.hd.message.dto.MessageSendStatus;
import com.hd.message.dto.MessageType;
import com.hd.message.entity.Message;
import com.hd.message.service.MessageDelivery;
import com.hd.message.util.CglibUtil;
import com.hd.message.util.HttpClientUtil;
import com.hd.message.util.StaxonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Observable;
import java.util.Observer;

/**
 * 短息通知服务提供者(观察者)
 *
 * @author wang_yw
 */
@Component("MessageProvider1")
public class SMSMessageProvider implements Observer, MessageDelivery {

    private final Logger logger = LoggerFactory.getLogger(SMSMessageProvider.class);

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public void send(MessageDTO messageDTO) {
        logger.info("发送短信消息，标题为：{}", messageDTO.getTitle());
        logger.info("发送短信消息，内容为：{}", messageDTO.getContent());

        Message message = new Message();
        CglibUtil.copyObject(messageDTO, message);

        String[] targets = messageDTO.getTargetUsers();
        for (int i = 0; i < targets.length; i++) {

            String target = targets[i];
            if (StringUtils.isEmpty(target)) {
                continue;
            }
            message.setMessageSendTarget(target);
            StringBuffer url = new StringBuffer();
            url.append("http://106.ihuyi.cn/webservice/sms.php?method=Submit&account=cf_heda&password=heda123!&mobile=");
            url.append(targets[i]);
            url.append("&content=");
            url.append(messageDTO.getContent());
            ResponseEntity<Object> responseEntity = HttpClientUtil.doGetRequest(url.toString());

            //1、判断是否请求成功
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                JSONObject result = JSONObject.parseObject(StaxonUtil.xml2json(responseEntity.getBody().toString()));

                //2、判断是否业务处理成功
                if (result.getJSONObject("SubmitResult").getInteger("code") == 2) {
                    logger.info("发送成功,目标：{}.", target);
                    message.setMessageSendStatus(MessageSendStatus.SUCCESS);
                } else {
                    logger.info("发送失败，目标：{};原因：{}", target, result.getJSONObject("SubmitResult").getString("msg"));
                    message.setMessageSendStatus(MessageSendStatus.FAIL);
                }
                messageRepository.save(messageDTO);
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
