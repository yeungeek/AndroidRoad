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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.disklrucache.DiskLruCache;
import com.yeungeek.basictraning.R;
import com.yeungeek.basictraning.fragments.BaseFragment;
import com.yeungeek.basictraning.util.SDCardUtil;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.LruCache;

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
                sb.append("size:").append(imageCache.size())
                        .append(", ").append(imageCache.toString()).append("\n")
                        .append("key:").append(key)
                        .append("\n");
                showText();
                break;
            case R.id.show_image:
                imageView.setImageBitmap(imageCache.get("ic_"));
                sb.append("get key ic_").append("\n");
                showText();
                break;
            case R.id.download_image:
                addFile(handler, "ic_" + SystemClock.elapsedRealtimeNanos());
                break;
            case R.id.show_download_image:
                getFile(handler, "ic_001");
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
}
