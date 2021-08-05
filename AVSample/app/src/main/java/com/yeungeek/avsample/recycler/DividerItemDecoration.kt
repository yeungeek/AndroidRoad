package com.yeungeek.videosample.recycler

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.yeungeek.avsample.R

class DividerItemDecoration() : RecyclerView.ItemDecoration(), Parcelable {
    private var mDivider: Drawable? = null
    private var mDividerHeight = 0
    private var inset = 0

    constructor(parcel: Parcel) : this() {
        mDividerHeight = parcel.readInt()
        inset = parcel.readInt()
    }

    constructor(context: Context, dividerHeight: Int) : this() {
        this.mDivider = ContextCompat.getDrawable(context, R.drawable.line_divider)
        this.mDividerHeight = dividerHeight
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.set(0, 0, 0, mDividerHeight)
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        val childCount = parent.childCount
        for (i in 0 until childCount - 1) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams

            val top = child.bottom + params.bottomMargin
            val bottom = top + mDivider!!.intrinsicHeight

            if (inset > 0) {
                mDivider!!.setBounds(left + inset, top, right - inset, bottom)
            } else {
                mDivider!!.setBounds(left, top, right, bottom)
            }
            mDivider!!.draw(c)

        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(mDividerHeight)
        parcel.writeInt(inset)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DividerItemDecoration> {
        override fun createFromParcel(parcel: Parcel): DividerItemDecoration {
            return DividerItemDecoration(parcel)
        }

        override fun newArray(size: Int): Array<DividerItemDecoration?> {
            return arrayOfNulls(size)
        }
    }
}