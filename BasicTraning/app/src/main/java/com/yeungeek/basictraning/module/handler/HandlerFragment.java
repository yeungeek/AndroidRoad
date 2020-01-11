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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yeungeek.basictraning.R;
import com.yeungeek.basictraning.fragments.BaseFragment;

/**
 * @author yangjian
 * @date 2018/11/06
 */

public class HandlerFragment extends BaseFragment implements View.OnClickListener {
    private Button createHandler;
    private Button sendMessage;
    private Button sendDelayMessage;
    private Button removeMessage;
    private TestHandlerThread testHandlerThread;


    private final static int MSG_DELAY = 0x01;

    private static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.d("DEBUG", "##### handleMessage: " + msg);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_handler, container, false);
        createHandler = view.findViewById(R.id.create_handler);
        sendMessage = view.findViewById(R.id.send_message);
        sendDelayMessage = view.findViewById(R.id.send_delay_message);
        removeMessage = view.findViewById(R.id.remove_message);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        testHandlerThread = new TestHandlerThread();

        sendDelayMessage.setOnClickListener(this);
        removeMessage.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_delay_message:
                Log.d("DEBUG", "##### send message");
                mHandler.sendEmptyMessageDelayed(MSG_DELAY, 2000);
                mHandler.sendEmptyMessageDelayed(MSG_DELAY, 5000);
                break;
            case R.id.remove_message:
                Log.d("DEBUG", "##### remove message");
                mHandler.removeMessages(MSG_DELAY);
                break;
        }
    }

    public class Test_HandlerThread extends HandlerThread {
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
