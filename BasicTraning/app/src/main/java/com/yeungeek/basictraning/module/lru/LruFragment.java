package com.yeungeek.basictraning.module.lru;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.LruCache;

import com.jakewharton.disklrucache.DiskLruCache;
import com.yeungeek.basictraning.R;
import com.yeungeek.basictraning.fragments.BaseFragment;
import com.yeungeek.basictraning.util.SDCardUtil;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Random;

/**
 * @date 2018/09/09
 */

public class LruFragment extends BaseFragment implements View.OnClickListener {
    private LruCache<String, Bitmap> imageCache;

    private Button addImage;
    private Button showImage;
    private Button downloadImage;
    private Button showDownloadImage;

    private ImageView imageView;
    private TextView textView;
    private StringBuilder sb = new StringBuilder();
    private Bitmap bitmap;

    private DiskLruCache diskLruCache;

    private Handler handler = new Handler(Looper.getMainLooper());

    //ImageCache
    private Chronometer mTimer;
    private Button mStartTimerBtn;
    private boolean isStart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_lru, container, false);
        addImage = view.findViewById(R.id.add_image);
        showImage = view.findViewById(R.id.show_image);
        downloadImage = view.findViewById(R.id.download_image);
        showDownloadImage = view.findViewById(R.id.show_download_image);

        imageView = view.findViewById(R.id.display_image);
        textView = view.findViewById(R.id.display_text);
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());

        addImage.setOnClickListener(this);
        showImage.setOnClickListener(this);
        downloadImage.setOnClickListener(this);
        showDownloadImage.setOnClickListener(this);

        mTimer = view.findViewById(R.id.timer);
        mStartTimerBtn = view.findViewById(R.id.start_time);
        mStartTimerBtn.setOnClickListener(this);

//        mTimer.setText(DateUtils.formatElapsedTime(14000L));
        mTimer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                Log.d("DEBUG", "##### onChronometerTick: " + chronometer.getText() + ",format: "
                        + chronometer.getFormat());
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        int maxCache = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxCache / 8;
        imageCache = new LruCache<String, Bitmap>(512) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount() / 1024;
            }

            @Override
            protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
                super.entryRemoved(evicted, key, oldValue, newValue);
                sb.append("remove:").append(key).append("\n");
                showText();
            }
        };

        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_wechat);
        imageCache.put("ic_", bitmap);

        try {
            diskLruCache = DiskLruCache.open(SDCardUtil.getDiskCacheDir(getActivity(), "imageCache"),
                    1, 1, 100 * 1024); //for test 100k
        } catch (IOException e) {
            e.printStackTrace();
        }

        addFile(handler, "ic_001");
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_image:
                String key = "ic_" + new Random().nextLong();
                imageCache.put(key, bitmap);
                Map<String, Bitmap> snapshot = imageCache.snapshot();
//                sb.append("size:").append(imageCache.size())
//                        .append(", ").append(imageCache.toString()).append("\n")
//                        .append("add key:").append(key)
//                        .append("\n");
                sb.append("\nsize:").append(imageCache.size()).append(",max size: ")
                        .append(imageCache.maxSize()).append("\n");
                sb.append("header: ").append(getEldest(snapshot).getKey()).append("\n");
                sb.append("map:======\n");
                sb.append(mapString(snapshot));
                showText();
                break;
            case R.id.show_image:
                imageView.setImageBitmap(imageCache.get("ic_"));
                sb.append("\nsize:").append(imageCache.size()).append("\n");
                sb.append("get key ic_").append("\n");
                sb.append("map:======\n");
                sb.append(mapString(imageCache.snapshot()));
                showText();
                break;
            case R.id.download_image:
                addFile(handler, "ic_" + SystemClock.elapsedRealtimeNanos());
                break;
            case R.id.show_download_image:
                getFile(handler, "ic_001");
                break;
            case R.id.start_time:
                mTimer.setBase(1470925223L);
                if (!isStart) {
                    isStart = true;
                    int hour = (int) ((SystemClock.elapsedRealtime() - mTimer.getBase()) / 1000 / 60);
                    Log.d("DEBUG", "##### start time: " + SystemClock.elapsedRealtime() + ", base: " + mTimer.getBase()
                            + ", hour: " + hour);

                    mTimer.setFormat("0" + hour + ":%s");
                    mTimer.start();
                } else {
                    mTimer.stop();
                }
                break;
        }
    }

    private void showText() {
        textView.setText(sb.toString());
        textView.post(new Runnable() {
            @Override
            public void run() {
                int scrollY = textView.getLayout().getLineTop(textView.getLineCount())
                        - textView.getHeight();

                if (scrollY > 0) {
                    textView.scrollTo(0, scrollY);
                } else {
                    textView.scrollTo(0, 0);
                }
            }
        });
    }

    private void getFile(final Handler handler, final String key) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DiskLruCache.Snapshot snapshot = diskLruCache.get(key);
                    InputStream inputStream = snapshot.getInputStream(0);
                    if (null == inputStream) {
                        return;
                    }

                    final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(bitmap);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void addFile(final Handler handler, final String key) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DiskLruCache.Editor editor = diskLruCache.edit(key);
                    if (null != editor) {
                        OutputStream os = editor.newOutputStream(0);
                        BufferedOutputStream bos = new BufferedOutputStream(os);
                        Bitmap res = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_wechat);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        res.compress(Bitmap.CompressFormat.PNG, 100, baos);

                        byte[] byteArray = baos.toByteArray();
                        bos.write(byteArray);
                        baos.close();
                        bos.close();

                        editor.commit();
                        diskLruCache.flush();

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                sb.append("size: ").append(diskLruCache.size() / 1024).append("\n");
                                sb.append("");
                                showText();
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private Map.Entry<String, Bitmap> getEldest(Map<String, Bitmap> map) {
        try {
            Method method = map.getClass().getMethod("eldest");
            Map.Entry<String, Bitmap> entry = (Map.Entry<String, Bitmap>) method.invoke(map);
            return entry;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String mapString(final Map<String, Bitmap> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Bitmap> entry : map.entrySet()) {
            sb.append("key:").append(entry.getKey()).append("\n");
        }
        return sb.toString();
    }
}
