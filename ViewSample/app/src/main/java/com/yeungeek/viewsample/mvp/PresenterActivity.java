package com.yeungeek.viewsample.mvp;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yeungeek.viewsample.R;
import com.yeungeek.viewsample.lifecycle.SharedViewModel;

/**
 * @author yangjian
 * @date 2018/08/17
 */

public class PresenterActivity extends AppCompatActivity {
    private IPresenter mPresenter;
    private static Handler uiHandler;
    private Button mSend;
    private TextView mDisplay;
    private TestHandlerThread testHandlerThread;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presenter);
        mPresenter = new BasePresenter();
        getLifecycle().addObserver(mPresenter);


        SharedViewModel viewModel = ViewModelProviders.of(this).get(SharedViewModel.class);
        Log.d("DEBUG", "" + viewModel);

        mSend = findViewById(R.id.send);
        mDisplay = findViewById(R.id.display);

        testHandlerThread = new TestHandlerThread();
        testHandlerThread.start();

        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                msg.what = 1;
                testHandlerThread.mHandler.sendMessage(msg);
            }
        });
        uiHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        mDisplay.setText((String) msg.obj);
                        break;
                    default:
                        break;
                }
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("DEBUG", "-----> PresenterActivity onDestroy");
    }

    public class TestHandlerThread extends Thread {
        private Handler mHandler;

        @Override
        public void run() {
            super.run();

            Looper.prepare();
            mHandler = new Handler(Looper.myLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    uiHandler.sendMessage( uiHandler.obtainMessage(1,"Handler Looper MessageQueue"));
                }
            };

            Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
                @Override
                public boolean queueIdle() {
                    return false;
                }
            });

            Looper.loop();
        }
    }
}
