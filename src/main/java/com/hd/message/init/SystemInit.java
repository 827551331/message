package com.hd.message.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 系统运行完毕之后初始化
 */
@Component
public class SystemInit implements CommandLineRunner {

    private static Logger logger = LoggerFactory.getLogger(SystemInit.class);

    @Override
    public void run(String... args) {
        logger.info("#启动消息消费者监听线程");
        ThreadPool.tpe.execute(new ConsumeThread());
    }
}
