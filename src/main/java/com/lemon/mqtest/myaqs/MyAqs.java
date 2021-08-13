package com.lemon.mqtest.myaqs;

import sun.misc.Unsafe;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.LockSupport;

public class MyAqs {

    //0未获取，1已获取
    private volatile int state;

    //偏移量
    private static long stateOffset;


    //当前持有
    private Thread lockHolder;

    private ConcurrentLinkedQueue<Thread> waiters = new ConcurrentLinkedQueue<>();

    private static final Unsafe unsafe = MyUnsafeInstance.reflectGetUnsafe();

    static {
        try {

            stateOffset = unsafe.objectFieldOffset(MyAqs.class.getDeclaredField("state"));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public final boolean compareAndSwapState(int old, int update) {
        return unsafe.compareAndSwapInt(this, stateOffset, old, update);
    }

    public boolean tryAcquire() {

        int state = getState();
        Thread thread = Thread.currentThread();
        if (state == 0 && (waiters == null || thread == waiters.peek()) && compareAndSwapState(0, 1)) {
// get the lock
            //持有锁的线程设置，为了重入，不用volatile
            setLockHolder(thread);
            return true;
        }
        return  false;

    }

    //没获取到就休眠
    public void lock(){

        if(tryAcquire()){
            return ;
        }
        //fail
        Thread thread = Thread.currentThread();
        waiters.add(thread);
        while(true){
            //再尝试便获取锁，为了防止老师休眠
            if(thread == waiters.peek() && tryAcquire()){
                //queue poll
                waiters.poll();
                return;
            }
            LockSupport.park(thread);
        }
    }

    public void unlock(){
        Thread thread = Thread.currentThread();
        if(thread == getLockHolder() && getState() == 1){
            //不需要cas,持有锁的线程操作中
            setLockHolder(null);
            this.state = 0;
            //唤醒一个线程
            Thread first = waiters.peek();
            if(first != null){
                LockSupport.unpark(first);
            }
        }
    }


    protected int getState() {
        return this.state;
    }

    public Thread getLockHolder() {
        return lockHolder;
    }

    public void setLockHolder(Thread lockHolder) {
        this.lockHolder = lockHolder;
    }

}
