package com.yeungeek.socket;

import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    public void testSocket() throws IOException {
        //1. 创建客户端
        Socket socket = new Socket("10.88.12.48", 8888);
        //2. 输出流
        OutputStream os = socket.getOutputStream();
        //3. 发送数据
        os.write("Hello world".getBytes());
        System.out.println("send message");
        os.flush();

        socket.shutdownOutput();

        os.close();
        socket.close();
    }
}