package com.hd.message.init;


import com.hd.message.dto.MessageDTO;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 消息队列
 *
 * @author wang_yw
 */
public class MessageQueue {

    /**
     * 初始化消息队列
     */
    private static BlockingQueue<MessageDTO> basket = new ArrayBlockingQueue<MessageDTO>(1 << 12);

    /**
     * 生产消息
     *
     * @param messageDTO
     */
    public static void produce(MessageDTO messageDTO) {
        try {
            basket.put(messageDTO);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * 同步消费消息
     *
     * @return
     */
    public static synchronized MessageDTO consume() {
        MessageDTO message = null;
        try {
            message = basket.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return message;
    }

}
