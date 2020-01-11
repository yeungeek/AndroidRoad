package com.yeungeek.hencoder.event1.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.yeungeek.hencoder.event1.R;

/**
 * @date 2019-04-30
 */

public class CustomSubView extends View {
    public CustomSubView(Context context) {
        super(context);
        init();
    }

    public CustomSubView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomSubView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
//        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("DEBUG", "##### CustomSubView action: " + event.getActionMasked());
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("DEBUG", "##### CustomSubView ACTION_DOWN");
                setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("DEBUG", "##### CustomSubView ACTION_MOVE");
//                setBackgroundColor(ContextCompat.getColor(getContext(), R.color.moveColor));
                break;
            case MotionEvent.ACTION_UP:
                Log.d("DEBUG", "##### CustomSubView ACTION_UP");
                setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d("DEBUG", "##### CustomSubView ACTION_CANCEL");
                setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                break;
        }
        return true;
    }

    //    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int measureWidth = getMeasuredWidth();
//        int measureHeight = getMeasuredHeight();
//
//        if (measureWidth < measureHeight) {
//            measureHeight = measureWidth;
//        } else {
//            measureWidth = measureHeight;
//        }
//
//        setMeasuredDimension(measureWidth, measureHeight);
//    }
}
