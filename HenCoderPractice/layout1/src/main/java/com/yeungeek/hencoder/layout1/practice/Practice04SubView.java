package com.yeungeek.hencoder.layout1.practice;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * @date 2019-04-30
 */

public class Practice04SubView extends View {
    public Practice04SubView(Context context) {
        super(context);
    }

    public Practice04SubView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice04SubView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }

        return true;
    }
}
