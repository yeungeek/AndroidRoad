package com.yeungeek.basicjava.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8888);
        System.out.println("server start");

        while (true) {
            Socket socket = serverSocket.accept();
            new ReadThread(socket).start();
        }
    }

    static class ReadThread extends Thread {
        BufferedReader bufferedReader;

        public ReadThread(Socket socket) throws IOException {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }

        @Override
        public void run() {
            super.run();
            String content = null;
            while (true) {
                while (true) {
                    try {
                        if (((content = bufferedReader.readLine()) != null)) {
                            System.out.println("Server: " + content);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }
}
