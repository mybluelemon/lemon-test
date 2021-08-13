package com.lemon.mqtest.myaqs;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

public class test {
    public static volatile  int count =0;

    public static void main(String[] args) throws InterruptedException {
        //test 十个线程，执行临界区代码，共累加1000
        MyAqs myAqs = new MyAqs();
        CountDownLatch countDownLatch = new CountDownLatch(10);
        for(int i = 0 ; i < 10 ; i++){
            new Thread(() ->{
                for(int j= 0 ; j< 1000; j++){
                    myAqs.lock();
                    danger();
                    myAqs.unlock();
                }
                countDownLatch.countDown();
            }).start();

        }
        countDownLatch.await();
        System.out.println(count);
    }
    public static void danger(){
//        MyAqs myAqs = new MyAqs();
//        myAqs.lock();
//        ReentrantLock lock = new ReentrantLock();
//        lock.unlock();
     count++;
//     lock.unlock();
//     myAqs.unlock();

    }
}
