package com.yeungeek.hencoder.draw1.practice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;

import com.yeungeek.hencoder.draw1.utils.AndroidUtils;

public class Practice3DrawRectView extends View {
    Pair<Integer, Integer> screenSize;
    private Paint mPaint;
    private int halfWidth;
    private int halfHeight;

    public Practice3DrawRectView(Context context) {
        super(context);
        init(context);
    }

    public Practice3DrawRectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Practice3DrawRectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        screenSize = AndroidUtils.getScreenSize(context);
        halfWidth = screenSize.first / 2;
        halfHeight = screenSize.second / 5;
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //练习内容：使用 canvas.drawRect() 方法画矩形
        canvas.drawRect(halfWidth - 200, halfHeight - 200,
                halfWidth + 200, halfHeight + 200, mPaint);


//        Rect rect = new Rect(halfWidth - 200, halfHeight - 200,
//                halfWidth + 200, halfHeight + 200);
//        canvas.drawRect(rect, mPaint);

//        RectF rectF = new RectF(halfWidth - 200, halfHeight - 200,
//                halfWidth + 200, halfHeight + 200);
//        canvas.drawRect(rectF, mPaint);
    }
}
