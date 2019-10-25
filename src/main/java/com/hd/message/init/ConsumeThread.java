package com.hd.message.init;

import com.hd.message.dto.MessageDTO;
import com.hd.message.service.impl.*;
import com.hd.message.util.SpringContextUtil;

/**
 * 消费者分配线程
 *
 * @author wang_yw
 */
public class ConsumeThread implements Runnable {


    @Override
    public void run() {
        while (true) {
            try {
                //从队列中消费消息，无消息时阻塞线程
                MessageDTO messageDTO = MessageQueue.consume();
                //从上线文中取出对象的对象
                MessageObserver messageObserver = SpringContextUtil.getBean(MessageObserver.class);
                messageObserver.registerObserver(SpringContextUtil.getBean(SYSMessageProvider.class));
                messageObserver.registerObserver(SpringContextUtil.getBean(HYSMSMessageProvider.class));
                messageObserver.registerObserver(SpringContextUtil.getBean(MailMessageProvider.class));
                messageObserver.registerObserver(SpringContextUtil.getBean(APPMessageProvider.class));
                messageObserver.setChanged();
                messageObserver.notifyAllObserver(messageDTO);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
