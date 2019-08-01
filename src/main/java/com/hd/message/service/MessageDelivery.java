package com.hd.message.service;

import com.hd.message.dto.MessageDTO;

/**
 * 消息投递
 *
 * @author wang_yw
 */
public interface MessageDelivery {

    public void send(MessageDTO messageDTO);
}
