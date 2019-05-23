package com.yeungeek.hencoder.layout1.practice;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

/**
 * @date 2019-04-30
 */

public class Practice04EventParentView extends ViewGroup {
    public Practice04EventParentView(Context context) {
        super(context);
    }

    public Practice04EventParentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice04EventParentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: //necessary
                return true;

        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
