package com.yeungeek.hencoder.draw1.practice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author yangjian
 * @date 2018/08/02
 */

public class Practice1DrawColorView extends View {
    public Practice1DrawColorView(Context context) {
        super(context);
    }

    public Practice1DrawColorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice1DrawColorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //练习内容：使用 canvas.drawColor() 方法把 View 涂成黄色
        //黄色： Color.YELLOW
//        canvas.drawColor(Color.YELLOW);

//        canvas.drawRGB(100, 200, 100);
        canvas.drawARGB(66,100, 200, 100);
    }
}
