package com.yeungeek.basictraning.module.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.yeungeek.basictraning.R;
import com.yeungeek.basictraning.fragments.BaseFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author yangjian
 * @date 2018/11/06
 */

public class HandlerFragment extends BaseFragment {
    private Button createHandler;
    private Button sendMessage;
    private TestHandlerThread testHandlerThread;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_handler, container, false);
        createHandler = view.findViewById(R.id.create_handler);
        sendMessage = view.findViewById(R.id.send_message);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        testHandlerThread = new TestHandlerThread();

        createHandler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testHandlerThread.start();
            }
        });

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.what = 1;

                testHandlerThread.getHandler().sendMessage(message);
            }
        });
    }

    public class Test_HandlerThread extends HandlerThread{
        public Test_HandlerThread(String name) {
            super(name);
        }
    }

    public class TestHandlerThread extends Thread {
        private Handler mHandler;

        @Override
        public void run() {
            Log.d("DEBUG", "###### TestHandlerThread run: " + Thread.currentThread().getId());
            Looper.prepare();
            mHandler = new Handler(Looper.myLooper(), new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    Log.d("DEBUG", "###### TestHandlerThread  Callback: " + Thread.currentThread().getId());
                    return false;
                }
            }) {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    Log.d("DEBUG", "###### TestHandlerThread  handleMessage: " + Thread.currentThread().getId());
                }
            };
            Looper.loop();
        }

        public Handler getHandler() {
            return mHandler;
        }
    }
}
