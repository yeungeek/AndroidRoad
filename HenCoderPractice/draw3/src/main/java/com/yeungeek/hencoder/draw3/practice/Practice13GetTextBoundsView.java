package com.yeungeek.hencoder.draw3.practice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class Practice13GetTextBoundsView extends View {
    Paint paint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint paint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
    String text1 = "A";
    String text2 = "a";
    String text3 = "J";
    String text4 = "j";
    String text5 = "Â";
    String text6 = "â";
    int top = 200;
    int bottom = 400;

    int text1Middle;
    int text2Middle;
    int text3Middle;
    int text4Middle;
    int text5Middle;
    int text6Middle;

    public Practice13GetTextBoundsView(Context context) {
        super(context);
    }

    public Practice13GetTextBoundsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice13GetTextBoundsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        paint1.setStyle(Paint.Style.STROKE);
        paint1.setStrokeWidth(20);
        paint1.setColor(Color.parseColor("#E91E63"));
        paint2.setTextSize(160);

        Rect textBounds = new Rect();

        paint2.getTextBounds(text1, 0, text1.length(), textBounds);
        text1Middle = (textBounds.top + textBounds.bottom) / 2;

        paint2.getTextBounds(text2, 0, text2.length(), textBounds);
        text2Middle = (textBounds.top + textBounds.bottom) / 2;


        paint2.getTextBounds(text3, 0, text3.length(), textBounds);
        text3Middle = (textBounds.top + textBounds.bottom) / 2;

        paint2.getTextBounds(text4, 0, text4.length(), textBounds);
        text4Middle = (textBounds.top + textBounds.bottom) / 2;


        paint2.getTextBounds(text5, 0, text5.length(), textBounds);
        text5Middle = (textBounds.top + textBounds.bottom) / 2;


        paint2.getTextBounds(text6, 0, text6.length(), textBounds);
        text6Middle = (textBounds.top + textBounds.bottom) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(50, top, getWidth() - 50, bottom, paint1);

        // 使用 Paint.getTextBounds() 计算出文字的显示区域
        // 然后计算出文字的绘制位置，从而让文字上下居中
        // 这种居中算法的优点是，可以让文字精准地居中，分毫不差

        int middle = (top + bottom) / 2;
        canvas.drawText(text1, 100, middle - text1Middle, paint2);
        canvas.drawText(text2, 200, middle - text2Middle, paint2);
        canvas.drawText(text3, 300, middle - text3Middle, paint2);
        canvas.drawText(text4, 400, middle - text4Middle, paint2);
        canvas.drawText(text5, 500, middle - text5Middle, paint2);
        canvas.drawText(text6, 600, middle - text6Middle, paint2);
    }
}