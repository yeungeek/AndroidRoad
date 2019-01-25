package com.yeungeek.hencoder.draw1.practice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;

import com.yeungeek.hencoder.draw1.utils.AndroidUtils;

public class Practice9DrawPathView extends View {
    Pair<Integer, Integer> screenSize;
    private Paint mPaint;
    private int halfWidth;
    private int halfHeight;
    private Path mPath;

    public Practice9DrawPathView(Context context) {
        super(context);
        init(context);
    }

    public Practice9DrawPathView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Practice9DrawPathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        screenSize = AndroidUtils.getScreenSize(context);
        halfWidth = screenSize.first / 2;
        halfHeight = screenSize.second / 5;
        mPaint = new Paint();

        mPath = new Path();

        mPath.addArc(new RectF(halfWidth - 120, halfHeight - 120,
                halfWidth, halfHeight), -210, 210);
        mPath.lineTo(halfWidth, halfHeight + 100);
        mPath.addArc(new RectF(halfWidth, halfHeight - 120,
                halfWidth + 120, halfHeight), 180, 210);
        mPath.lineTo(halfWidth, halfHeight + 100);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        练习内容：使用 canvas.drawPath() 方法画心形

        canvas.drawPath(mPath, mPaint);
    }
}
