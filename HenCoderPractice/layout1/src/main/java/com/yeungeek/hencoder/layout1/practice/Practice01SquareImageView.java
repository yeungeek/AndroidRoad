package com.yeungeek.hencoder.layout1.practice;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * 需要把它写成正方形的 ImageView
 */
public class Practice01SquareImageView extends AppCompatImageView {
    public Practice01SquareImageView(Context context) {
        super(context);
    }

    public Practice01SquareImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice01SquareImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 重写 onMeasure()，调整尺寸，让 ImageView 总是正方形
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 先用 getMeasuredWidth() 和 getMeasuredHeight() 取到 super.onMeasure() 的计算结果
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        // 然后通过计算，让宽度和高度一致
        if (width < height) {
            height = width;
        } else {
            width = height;
        }
        // 再用 setMeasuredDimension(width, height) 来保存最终的宽度和高度
        setMeasuredDimension(width, height);
    }
}
