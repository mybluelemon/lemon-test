package com.lemon.mqtest.threadPool;

import io.netty.handler.codec.serialization.ObjectEncoder;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

public interface DenyPolicy {
    //缓存队列导到上线，如何通知提交者
    void reject(Runnable runnable, LemonThreadPool threadPool );

    class DiscardDenyPolicy implements DenyPolicy {
        @Override
        public void reject(Runnable runnable, LemonThreadPool threadPool) {
            //do nothing ,不通知
            ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor();
            threadPoolExecutor.submit()
            ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
            ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
            ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);
            WeakReference<ObjectEncoder> weakReference =  new WeakReference(new ObjectEncoder());

        }
    }

    class AbortDenyPolicy implements DenyPolicy {
        @Override
        public void reject(Runnable runnable, LemonThreadPool threadPool) {
            throw new RuntimeException();
        }
    }
    class RunnerDenyPlicy implements  DenyPolicy {

        @Override
        public void reject(Runnable runnable, LemonThreadPool threadPool) {
if(!threadPool.isShutDown()){
    runnable.run();
}
        }
    }

}
