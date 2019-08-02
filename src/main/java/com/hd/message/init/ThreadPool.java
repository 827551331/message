package com.hd.message.init;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 初始化线程池
 *
 * @author wang_yw
 */
public class ThreadPool {

    public static ThreadPoolExecutor tpe = new ThreadPoolExecutor(5, 50, 5, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(200));
}
