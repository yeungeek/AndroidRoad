package com.yeungeek.jnisample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = findViewById(R.id.sample_text);
        tv.setText(NativeHelper.stringFromJNI());
        final SocketHelper socketHelper = new SocketHelper();

        findViewById(R.id.sent_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socketHelper.startClient("10.88.12.48", 8888);
            }
        });

//        NativeHelper.callStaticMethod(10);
//        new NativeHelper().callMethod(1);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
}
