package com.yeungeek.hencoder.event1.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.yeungeek.hencoder.event1.R;

/**
 * @date 2019-04-30
 */

public class CustomParentView extends LinearLayout {
    public CustomParentView(Context context) {
        super(context);
    }

    public CustomParentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomParentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("DEBUG", "##### CustomParentView action: " + event.getActionMasked());
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("DEBUG", "##### CustomParentView ACTION_DOWN");
                setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("DEBUG", "##### CustomParentView ACTION_MOVE");
//                setBackgroundColor(ContextCompat.getColor(getContext(), R.color.moveColor));
                break;
            case MotionEvent.ACTION_UP:
                Log.d("DEBUG", "##### CustomParentView ACTION_UP");
                setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d("DEBUG", "##### CustomParentView ACTION_CANCEL");
                setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                break;
        }
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                return true;
        }
        return super.onInterceptTouchEvent(ev);
    }
}
