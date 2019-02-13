package com.yeungeek.hencoder.draw3.practice;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

@TargetApi(Build.VERSION_CODES.M)
public class Practice02StaticLayoutView extends View {
    TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    String text = "Hello\nHenCoder";
    StaticLayout staticLayout;

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public Practice02StaticLayoutView(Context context) {
        super(context);
    }

    public Practice02StaticLayoutView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice02StaticLayoutView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        textPaint.setTextSize(60);
        staticLayout = new StaticLayout(text, textPaint, 600, Layout.Alignment.ALIGN_LEFT, 1, 0, true);

        paint.setColor(Color.GREEN);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        canvas.drawRect(0, 0, width, height, paint);
        // 使用 StaticLayout 代替 Canvas.drawText() 来绘制文字，
        // 以绘制出带有换行的文字
//        canvas.drawText(text, 50, 100, textPaint);
        canvas.save();
        canvas.translate(50, 100);
        staticLayout.draw(canvas);
        canvas.restore();

        circlePaint.setColor(Color.RED);
        canvas.drawCircle(width - 70, height - 120, 10, circlePaint);
    }
}
