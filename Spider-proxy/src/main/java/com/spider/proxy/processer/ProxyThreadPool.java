package com.spider.proxy.processer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by wangke on 2017/10/9.
 */
public class ProxyThreadPool {
    private static ProxyThreadPool mInstance;
    private ThreadPoolExecutor mThreadPoolExec;
    private static final int MAX_POOL_SIZE = 10;
    private static final int KEEP_ALIVE = 20;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();

    public static synchronized void post(Runnable runnable) {
        if (mInstance == null) {
            mInstance = new ProxyThreadPool();
        }
        System.out.println("PoolSize:" + mInstance.mThreadPoolExec.getPoolSize() + ","
                + "ActiveCount:" + mInstance.mThreadPoolExec.getActiveCount() + ","
                + "QueueSize:" + mInstance.mThreadPoolExec.getQueue().size() + ","
                + "TaskCount:" + mInstance.mThreadPoolExec.getTaskCount());
        mInstance.mThreadPoolExec.execute(runnable);
    }

    private ProxyThreadPool() {
        int coreNum = Runtime.getRuntime().availableProcessors();
        mThreadPoolExec = new ThreadPoolExecutor(
                coreNum,
                MAX_POOL_SIZE,
                KEEP_ALIVE,
                TimeUnit.SECONDS,
                workQueue);
    }

    public static void finish() {
        mInstance.mThreadPoolExec.shutdown();
    }
}
