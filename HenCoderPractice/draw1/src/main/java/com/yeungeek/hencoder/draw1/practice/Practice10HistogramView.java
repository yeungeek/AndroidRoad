package com.yeungeek.hencoder.draw1.practice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;

import com.yeungeek.hencoder.draw1.utils.AndroidUtils;

public class Practice10HistogramView extends View {
    Pair<Integer, Integer> screenSize;
    private Paint mPaint;
    private int halfWidth;
    private int halfHeight;

    private int lineHeight;

    private Path mPath;

    public Practice10HistogramView(Context context) {
        super(context);
        init(context);
    }

    public Practice10HistogramView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Practice10HistogramView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        screenSize = AndroidUtils.getScreenSize(context);
        halfWidth = screenSize.first / 2;
        halfHeight = screenSize.second / 5;
        mPaint = new Paint();
        mPath = new Path();
        lineHeight = halfHeight * 2 - 180;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        综合练习
//        练习内容：使用各种 Canvas.drawXXX() 方法画直方图

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.WHITE);
        mPath.moveTo(150, 50);
        mPath.lineTo(150, lineHeight);
        mPath.lineTo(screenSize.first - 150, lineHeight);
        canvas.drawPath(mPath, mPaint);


        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor("#73ba0e"));

        canvas.drawRect(180, lineHeight - 5, 260, lineHeight, mPaint);
        canvas.drawRect(280, lineHeight - 15, 360, lineHeight, mPaint);
        canvas.drawRect(380, lineHeight - 20, 460, lineHeight, mPaint);
        canvas.drawRect(480, lineHeight - 180, 560, lineHeight, mPaint);
        canvas.drawRect(580, lineHeight - 280, 660, lineHeight, mPaint);
        canvas.drawRect(680, lineHeight - 300, 760, lineHeight, mPaint);
        canvas.drawRect(780, lineHeight - 150, 860, lineHeight, mPaint);
//        mPath.moveTo(170, lineHeight);
//        mPath.lineTo(170, lineHeight - 10);
//        mPath.lineTo(220, lineHeight - 10);
//        mPath.lineTo(220, lineHeight);
//        canvas.drawPath(mPath, mPaint);

        mPaint.setColor(Color.WHITE);

        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(25);
        canvas.drawText("Froyo", 220, lineHeight + 30, mPaint);
        canvas.drawText("GB", 320, lineHeight + 30, mPaint);
        canvas.drawText("ICS", 420, lineHeight + 30, mPaint);
        canvas.drawText("JB", 520, lineHeight + 30, mPaint);
        canvas.drawText("KitKat", 620, lineHeight + 30, mPaint);
        canvas.drawText("L", 720, lineHeight + 30, mPaint);
        canvas.drawText("M", 820, lineHeight + 30, mPaint);

        mPaint.setTextSize(45);
        canvas.drawText("直方图", halfWidth, halfHeight * 2 - 50, mPaint);
    }
}
