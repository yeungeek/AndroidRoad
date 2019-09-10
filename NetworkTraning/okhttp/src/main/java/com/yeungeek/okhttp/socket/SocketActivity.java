package com.yeungeek.okhttp.socket;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yeungeek.okhttp.R;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * @date 2019-09-09
 */

public class SocketActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mSendBtn;
    private TextView mSendTv;

    private OkHttpClient mHttpClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);

        mSendBtn = findViewById(R.id.send_btn);
        mSendTv = findViewById(R.id.send_tv);

        mSendBtn.setOnClickListener(this);

        initSocket();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_btn:
                start();
                break;
        }
    }

    private void initSocket() {
        mHttpClient = new OkHttpClient.Builder().build();
    }

    private void start() {
        Request request = new Request
                .Builder()
                .url("ws://echo.websocket.org")
                .build();

        WebSocket ws = mHttpClient.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
                webSocket.send("Hello Socket Server!");
                webSocket.send("HH!");
                webSocket.send(ByteString.decodeHex("deadbeef"));
                webSocket.close(1000, "Bye!");
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);
                println("onMessage: " + text);
                Log.d("DEBUG", "##### onMessage: " + text);
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                super.onMessage(webSocket, bytes);
                println("onMessage bytes: " + bytes.hex());
                Log.d("DEBUG", "##### onMessage bytes: " + bytes.hex());
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                super.onClosing(webSocket, code, reason);
                webSocket.close(1000, null);
                println("onClosing: code " + code + ",reason: " + reason);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
                println("onClosed: code " + code + ",reason: " + reason);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                super.onFailure(webSocket, t, response);
                println("onFailure: " + t.getMessage());
                Log.e("DEBUG", "##### onFailure: " + t.getMessage(), t);
            }
        });
//        mHttpClient.dispatcher().executorService().shutdown();
    }

    private void println(final String str) {
        runOnUiThread(() -> {
            mSendTv.setText(str);
        });
    }
}
