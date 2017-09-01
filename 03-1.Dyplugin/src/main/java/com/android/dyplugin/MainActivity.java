package com.android.dyplugin;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.id_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickBtn(getApplicationContext(), "----> click from activity");
            }
        });
    }

    public void clickBtn(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
