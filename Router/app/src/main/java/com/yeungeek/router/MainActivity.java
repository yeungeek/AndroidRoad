package com.yeungeek.router;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.yeungeek.router.annotation.RouterUri;
import com.yeungeek.router.api.Router;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private RouterService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.start_first).setOnClickListener(this);

        service = new Router.Builder()
                .setContext(this)
                .build()
                .create(RouterService.class);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_first:
//                startIntent();
                service.startFirstActivity();
                break;
        }
    }

    private void startIntent() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("router://com.yeungeek.router.firstactivity"));
        PackageManager manager = getPackageManager();
        List<ResolveInfo> infoList = manager.queryIntentActivities(intent, 0);
        if (!infoList.isEmpty()) {
            startActivity(intent);
        }
    }
}
