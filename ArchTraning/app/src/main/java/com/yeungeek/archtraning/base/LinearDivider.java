package com.yeungeek.archtraning.base;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author yangjian
 * @date 2018/09/20
 */

public class LinearDivider extends RecyclerView.ItemDecoration {
    private final Drawable mDivider;
    private final int mSize;
    private final int mOrientation;

    public LinearDivider(Context context, @ColorRes int color, @DimenRes int size) {
        mDivider = new ColorDrawable(context.getResources().getColor(color));
        mSize = context.getResources().getDimensionPixelSize(size);
        mOrientation = LinearLayoutManager.VERTICAL;
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int left, right, bottom, top;

        if (mOrientation == LinearLayoutManager.VERTICAL) {
            left = parent.getPaddingLeft();
            right = parent.getWidth() - parent.getPaddingRight();
            for (int i = 0; i < parent.getChildCount() - 1; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                top = child.getBottom() + params.bottomMargin;
                bottom = top + mSize;
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        } else {
            top = parent.getPaddingTop();
            bottom = parent.getHeight() - parent.getPaddingBottom();
            for (int i = 0; i < parent.getChildCount() - 1; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                left = child.getRight() + params.rightMargin;
                right = left + mSize;
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            outRect.set(0, 0, 0, mSize);
        } else {
            outRect.set(0, 0, mSize, 0);
        }
    }
}
