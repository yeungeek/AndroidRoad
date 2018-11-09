package com.yeungeek.modulartraning.block;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * @author yangjian
 * @date 2018/10/05
 */

public class PageBlock extends FrameLayout implements LifecycleObserver {
    private int pageId;
    private TypeFactory factory;

    private TextView loadingStatus;

    public PageBlock(@NonNull Context context) {
        super(context);
    }

    public PageBlock(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PageBlock(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PageBlock(Context context, int pageId, TypeFactory factory) {
        super(context);
        init();

        this.pageId = pageId;
        this.factory = factory;
    }

    private void init() {
        ((LifecycleOwner) getContext()).getLifecycle().addObserver(this);
        loadingStatus = new TextView(getContext());
        addView(loadingStatus);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        Log.d("DEBUG", "====== onStart");
        loadingStatus.setText("start...");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        Log.d("DEBUG", "====== onResume");
        loadingStatus.setText("resume pageId: " + pageId);
        factory.getList("onResume");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        Log.d("DEBUG", "====== onDestroy");
        ((LifecycleOwner) getContext()).getLifecycle().removeObserver(this);
    }

    public static class Builder {
        private int pageId;
        private TypeFactory factory;

        public Builder setPageId(int pageId) {
            this.pageId = pageId;
            return this;
        }

        public Builder setFactory(TypeFactory factory) {
            this.factory = factory;
            return this;
        }

        public PageBlock build(Context context) {
            return new PageBlock(context, pageId, factory);
        }
    }

    /**
     * for outer
     */
    public interface TypeFactory {
        void getList(String pageName);
    }
}
