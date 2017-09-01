package com.android.dynamical;

import android.content.Context;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.load_apk).setOnClickListener(this);
        findViewById(R.id.load_dex).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.load_apk:
                loadApk();
                break;
            case R.id.load_dex:
                loadDex();
                break;
        }
    }

    private void loadApk() {
        String pluginFile = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "plugin.apk";
        if (copyFileFromAssets(this, "plugin.apk", pluginFile)) {
            try {
                File optimizedDirectoryFile = getCacheDir();
                DexClassLoader classLoader = new DexClassLoader(pluginFile,
                        optimizedDirectoryFile.getAbsolutePath(), null, getClassLoader());

                //reflect
                Class loadClazz = classLoader.loadClass("com.android.dyplugin.MainActivity");
                Object instance = loadClazz.getConstructor(new Class[]{}).newInstance(new Object[]{});

                Method[] methods = loadClazz.getDeclaredMethods();
                for (Method method : methods) {
                    Timber.d("----> method: %s", method.toString());
                }

                Method clickMethod = loadClazz.getDeclaredMethod("clickBtn", Context.class, String.class);
                clickMethod.setAccessible(true);
                clickMethod.invoke(instance, new Object[]{this, "----> Loading from plugin apk"});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void loadDex() {
        String pluginFile = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "plugin.dex";
        if (copyFileFromAssets(this, "plugin.dex", pluginFile)) {
            try {
                File optimizedDirectoryFile = getDir("dex", 0);
                DexClassLoader classLoader = new DexClassLoader(pluginFile,
                        optimizedDirectoryFile.getAbsolutePath(), null, getClassLoader());
                Class loadClazz = classLoader.loadClass("com.android.dyplugin.Foo");
                Method foo = loadClazz.getDeclaredMethod("foo");
                foo.setAccessible(true);
                String fooString = (String) foo.invoke(loadClazz.newInstance());

                Toast.makeText(this, fooString, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    private boolean copyFileFromAssets(Context context, String fileName, String path) {
        boolean isFinish = false;
        try {
            InputStream is = context.getAssets().open(fileName);
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
                FileOutputStream outputStream = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int i = 0;
                while ((i = is.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, i);
                }

                outputStream.close();
                is.close();
                isFinish = true;
            } else {
                isFinish = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isFinish;
    }
}
