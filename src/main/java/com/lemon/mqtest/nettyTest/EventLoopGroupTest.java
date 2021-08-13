package com.lemon.mqtest.nettyTest;

import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class EventLoopGroupTest {
    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger();
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup(2);
        for(int i = 0 ; i < 10; i++) {
            nioEventLoopGroup.execute(() -> {
                log.debug("task" + atomicInteger.incrementAndGet());
                try {
                    Thread.sleep(1 * 1000 );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
