package com.yeungeek.hencoder.draw1.practice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;

import com.yeungeek.hencoder.draw1.utils.AndroidUtils;

public class Practice5DrawOvalView extends View {
    Pair<Integer, Integer> screenSize;
    private Paint mPaint;
    private int halfWidth;
    private int halfHeight;

    public Practice5DrawOvalView(Context context) {
        super(context);
        init(context);
    }

    public Practice5DrawOvalView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Practice5DrawOvalView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        screenSize = AndroidUtils.getScreenSize(context);
        halfWidth = screenSize.first / 2;
        halfHeight = screenSize.second / 5;
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        练习内容：使用 canvas.drawOval() 方法画椭圆
        canvas.drawOval(new RectF(halfWidth - 200, halfHeight - 100,
                halfWidth + 200, halfHeight + 120), mPaint);
    }
}
