package com.yeungeek.hencoder.draw1.practice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;

import com.yeungeek.hencoder.draw1.utils.AndroidUtils;

public class Practice8DrawArcView extends View {
    Pair<Integer, Integer> screenSize;
    private Paint mPaint;
    private int halfWidth;
    private int halfHeight;
    private Path mPath;

    public Practice8DrawArcView(Context context) {
        super(context);
        init(context);
    }

    public Practice8DrawArcView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Practice8DrawArcView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        screenSize = AndroidUtils.getScreenSize(context);
        halfWidth = screenSize.first / 2;
        halfHeight = screenSize.second / 5;
        mPaint = new Paint();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        练习内容：使用 canvas.drawArc() 方法画弧形和扇形
        mPaint.setStyle(Paint.Style.FILL);

        canvas.drawArc(halfWidth - 200, halfHeight - 100,
                halfWidth + 200, halfHeight + 100, 40, 100, true, mPaint);


        canvas.drawArc(halfWidth - 200, halfHeight - 100,
                halfWidth + 200, halfHeight + 100, -100, 100, false, mPaint);

        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(halfWidth - 200, halfHeight - 100,
                halfWidth + 200, halfHeight + 100, 150, 60, false, mPaint);
    }
}
