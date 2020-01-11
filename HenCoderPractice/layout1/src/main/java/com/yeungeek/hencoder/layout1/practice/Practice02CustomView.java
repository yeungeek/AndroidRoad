package com.yeungeek.hencoder.layout1.practice;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * @date 2019-04-29
 */

public class Practice02CustomView extends View {
    public Practice02CustomView(Context context) {
        super(context);
    }

    public Practice02CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice02CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);

        int measureWidth = getMeasuredWidth();
        int measureHeight = getMeasuredHeight();

        measureWidth = resolveSize(measureWidth, widthMeasureSpec);
        measureHeight = resolveSize(measureHeight, heightMeasureSpec);

        setMeasuredDimension(measureWidth, measureHeight);
    }
}
