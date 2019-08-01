package com.hd.message.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hd.message.dto.MessageDTO;
import com.hd.message.dto.MessageType;
import com.hd.message.service.MessageDelivery;
import com.hd.message.util.HttpClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Observable;
import java.util.Observer;

/**
 * 系统通知服务提供者(观察者)
 *
 * @author wang_yw
 */
@Component("MessageProvider2")
public class SYSMessageProvider implements Observer, MessageDelivery {

    private final Logger logger = LoggerFactory.getLogger(SYSMessageProvider.class);

    @Value("${integration-url}")
    private String uniwater_url;

    @Override
    public void send(MessageDTO messageDTO) {

        JSONObject param = new JSONObject();
        param.put("access_token", messageDTO.getToken());
        param.put("type", "alarm");
        param.put("event", JSONObject.toJSON(messageDTO));
        String url = uniwater_url + "/hdl/uniwater/v1.0/event/push.json";
        ResponseEntity<Object> responseEntity = HttpClientUtil.postRequest(url, param.toJSONString());
        JSONObject result = JSONObject.parseObject(responseEntity.getBody().toString());
        if (result.getInteger("Code") == 0) {
            logger.info("系统消息推送成功，消息标题：{}，消息ID：{}", messageDTO.getTitle(), result.getString("Response"));
        } else {
            logger.error("系统消息推送失败，消息标题：{}，失败原因：{}", messageDTO.getTitle(), result.getString("Message"));
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        MessageDTO messageDTO = (MessageDTO) arg;
        //判断消息是否需要短信发送
        if (messageDTO.getMessageType() == MessageType.SYSMSG || messageDTO.getMessageType() == MessageType.ALLMSG) {
            this.send(messageDTO);
        }
    }
}
