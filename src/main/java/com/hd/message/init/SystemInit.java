package com.hd.message.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 系统运行完毕之后初始化
 */
@Component
public class SystemInit implements CommandLineRunner {

    private static Logger logger = LoggerFactory.getLogger(SystemInit.class);

    /**
     * 消息消费线程监听数量（会影响并发量，受限制于宿主主机配置（CPU核心数等））
     */
    @Value("${send-thread-num}")
    private Integer num;

    @Override
    public void run(String... args) {
        logger.info("#启动{}个消息消费者监听线程", num);
        for (int i = 0; i < num; i++) {
            ThreadPool.tpe.execute(new ConsumeThread());
        }
    }
}
