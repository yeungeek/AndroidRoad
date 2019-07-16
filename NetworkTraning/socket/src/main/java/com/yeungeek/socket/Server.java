package com.yeungeek.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author jian.yang
 * @date 2019-07-11
 */

public class Server {
    public static void main(String[] args) throws IOException {
        //1. 创建ServerSocket
        ServerSocket serverSocket = new ServerSocket(8888);
        //2. 监听
        Socket socket = serverSocket.accept();
        System.out.println("server start listen");
        //3. 输入流
        InputStream is = socket.getInputStream();
        InputStreamReader reader = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(reader);
        String content = null;
        StringBuffer sb = new StringBuffer();
        while ((content = br.readLine()) != null) {
            sb.append(content);
        }

        System.out.println("server receiver: " + sb.toString());

        socket.shutdownInput();

        br.close();
        reader.close();
        is.close();

        socket.close();
        serverSocket.close();
    }
}
