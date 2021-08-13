package com.lemon.mqtest.nettyTest;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class JAVANIO {

    public static void main(String[] args) {
        String property = System.getProperty("user.dir");
        System.out.println(property);
        try (RandomAccessFile file = new RandomAccessFile("D:/HOSTS", "rw")) {

            ByteBuffer buffer = ByteBuffer.allocate(10);
            FileChannel fileChannel = file.getChannel();
            while (true) {
                int count = fileChannel.read(buffer);
                if (count == -1) {
                    break;
                }
                buffer.flip();
                while (buffer.hasRemaining()) {
                    System.out.print((char) buffer.get());
                }
                // 切换 buffer 写模式
                buffer.clear();
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
