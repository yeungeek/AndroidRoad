package com.yeungeek.hencoder.draw2.practice;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import com.yeungeek.hencoder.draw2.R;

public class Practice08XfermodeView extends View {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Bitmap bitmap1;
    Bitmap bitmap2;

    public Practice08XfermodeView(Context context) {
        super(context);
    }

    public Practice08XfermodeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice08XfermodeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.batman);
        bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.batman_logo);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 使用 paint.setXfermode() 设置不同的结合绘制效果

        // 别忘了用 canvas.saveLayer() 开启 off-screen buffer

        int saved = canvas.saveLayer(null, null);

        canvas.drawBitmap(bitmap1, 0, 0, paint);
        // 第一个：PorterDuff.Mode.SRC
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap2, 0, 0, paint);
        paint.setXfermode(null);

        canvas.drawBitmap(bitmap1, bitmap1.getWidth() + 100, 0, paint);
        // 第二个：PorterDuff.Mode.DST_IN
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawBitmap(bitmap2, bitmap1.getWidth() + 100, 0, paint);
        paint.setXfermode(null);

        canvas.drawBitmap(bitmap1, 0, bitmap1.getHeight() + 20, paint);
        // 第三个：PorterDuff.Mode.DST_OUT
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        canvas.drawBitmap(bitmap2, 0, bitmap1.getHeight() + 20, paint);
        paint.setXfermode(null);

        // 用完之后使用 canvas.restore() 恢复 off-screen buffer
        canvas.restoreToCount(saved);
    }
}
