package com.yeungeek.hencoder.layout1.practice;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * @date 2019-04-29
 */

public class Practice03LayoutView extends ViewGroup {
    public Practice03LayoutView(Context context) {
        super(context);
    }

    public Practice03LayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice03LayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //multi measure
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);

            int childWidthSpce;
            int usedSize = 0;
            LayoutParams lp = childView.getLayoutParams();
            int selfWidthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
            int selfWidthSpecSize = MeasureSpec.getSize(heightMeasureSpec);

            switch (lp.width) {
                case MATCH_PARENT:
                    if (selfWidthSpecMode == MeasureSpec.EXACTLY || selfWidthSpecMode == MeasureSpec.AT_MOST) {
                        childWidthSpce = MeasureSpec.makeMeasureSpec(selfWidthSpecSize - usedSize, MeasureSpec.EXACTLY);
                    } else {
                        childWidthSpce = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
                    }
                    break;
                case WRAP_CONTENT:
                    if (selfWidthSpecMode == MeasureSpec.EXACTLY || selfWidthSpecMode == MeasureSpec.AT_MOST) {
                        childWidthSpce = MeasureSpec.makeMeasureSpec(selfWidthSpecSize - usedSize, MeasureSpec.AT_MOST);
                    } else {
                        childWidthSpce = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
                    }
                    break;
                default:
                    childWidthSpce = MeasureSpec.makeMeasureSpec(lp.width, MeasureSpec.EXACTLY);
                    break;
            }


        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
//            childView.layout();
        }
    }
}
