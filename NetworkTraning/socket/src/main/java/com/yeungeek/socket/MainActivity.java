package com.yeungeek.socket;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import java.io.OutputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.send_message).setOnClickListener(this);
    }

    private void sendMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_message:
                sendMessage();
                break;
        }
    }
}
