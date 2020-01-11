package com.yeungeek.viewsample.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * @author yangjian
 * @date 2018/08/12
 */

public class LabelsView extends ViewGroup {
    public LabelsView(Context context) {
        super(context);
    }

    public LabelsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LabelsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d("LabelsView", "===== onLayout");
        int x = getPaddingLeft();
        int y = getPaddingRight();

        int contentWidth = r - l;
        int maxtItemHeight = 0;

        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);

            if (contentWidth < x + view.getMeasuredWidth() + getPaddingRight()) {
                x = getPaddingLeft();
                y += maxtItemHeight;
                maxtItemHeight = 0;
            }

            view.layout(x, y, x + view.getMeasuredWidth(), y + view.getMeasuredHeight());
            x += view.getMeasuredWidth();
            maxtItemHeight = Math.max(maxtItemHeight, view.getMeasuredHeight());
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d("LabelsView", "===== onMeasure");
        int count = getChildCount();
        int maxWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();

        int contentHeight = 0;
        int lineWidth = 0;
        int maxLineWidth = 0;
        int maxItemHeight = 0;

        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            measureChild(view, widthMeasureSpec, heightMeasureSpec);

            if (maxWidth < lineWidth + view.getMeasuredWidth()) {
                contentHeight += maxItemHeight;
                maxItemHeight = 0;
                maxLineWidth = Math.max(maxLineWidth, lineWidth);
                lineWidth = 0;
            }

            maxItemHeight = Math.max(maxItemHeight, view.getMeasuredHeight());
            lineWidth += view.getMeasuredWidth();
        }

        contentHeight += maxItemHeight;
        maxLineWidth = Math.max(maxLineWidth, lineWidth);

        setMeasuredDimension(measureWidth(widthMeasureSpec, maxLineWidth),
                measureHeight(heightMeasureSpec, contentHeight));
    }

    private int measureWidth(int measureSpec, int contentWidth) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = contentWidth + getPaddingLeft() + getPaddingRight();
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }

        result = Math.max(result, getSuggestedMinimumWidth());

        return result;
    }

    private int measureHeight(int measureSpec, int contentHeight) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = contentHeight + getPaddingTop() + getPaddingBottom();
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }

        result = Math.max(result, getSuggestedMinimumHeight());
        return result;
    }


    public void addLabels(List<String> labels) {
        for (String label : labels) {
            TextView tv = new TextView(getContext());
            tv.setText(label);
            tv.setTextSize(16);
            tv.setPadding(10, 10, 10, 10);

            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(params);

            tv.setTextColor(Color.parseColor("#000000"));
//            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            addView(tv);
        }
    }
}
