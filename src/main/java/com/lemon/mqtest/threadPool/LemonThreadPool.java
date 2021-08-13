package com.lemon.mqtest.threadPool;

public interface LemonThreadPool {

    void execute(Runnable runnable);

    //shut down
    void shutDown();

    //init size
    int getInitSize();

    //max size
    int getMaxSize();

    //core size
    int getCoreSize();

    //active count
    int getActiveCount();

    //队列
    int getQueueSize();

    boolean isShutDown();

}
