package com.lemon.mqtest.threadPool;

//队列里的任务
public interface RunnableQueue {

    void offer(Runnable runnable);

    Runnable take();

    int size();


}
