package com.yeungeek.views.flow.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import timber.log.Timber;

/**
 * Created by yeungeek on 2016/10/21.
 */

public class CustomView extends View {
    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Timber.d("----> onMeasure");
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int widthMeasureSpec) {
        int width = 0;
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);

        if (specMode == MeasureSpec.EXACTLY) {  //match_parent
            width = specSize;
        } else {
            width = 200;
            if (specMode == MeasureSpec.AT_MOST) {  //wrap_parent
                width = Math.min(width, specSize);
            }
        }

        Timber.d("----> onMeasure width: %d", width);
        return width;
    }

    private int measureHeight(int heightMeasureSpec) {
        int height = 0;
        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        int specSize = MeasureSpec.getSize(heightMeasureSpec);

        if (specMode == MeasureSpec.EXACTLY) {  //match_parent
            height = specSize;
        } else {
            height = 200;
            if (specMode == MeasureSpec.AT_MOST) {  //wrap_parent
                height = Math.min(height, specSize);
            }
        }

        Timber.d("----> onMeasure height: %d", height);
        return height;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Timber.d("----> onLayout");
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        Timber.d("----> draw");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Timber.d("----> onDraw");
    }
}
