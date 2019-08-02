package com.yeungeek.basicjava.nio;

import sun.nio.ch.DirectBuffer;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

public class DirectByteBufferTest {
    public static void main(String[] args) throws InterruptedException {
//        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024 * 1024 * 128);
        DirectBuffer byteBuffer = (DirectBuffer)ByteBuffer.allocateDirect(1024 * 1024 * 512);
        TimeUnit.SECONDS.sleep(10);
        //clean
        byteBuffer.cleaner().clean();

        TimeUnit.SECONDS.sleep(10);

        System.out.println("run ok");
    }
}
