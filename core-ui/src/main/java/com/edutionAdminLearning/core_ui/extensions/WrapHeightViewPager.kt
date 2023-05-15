package com.edutionAdminLearning.core_ui.extensions

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.viewpager.widget.ViewPager


class WrapHeightViewPager : ViewPager {
    private var currentPosition = 0
    private var swipeEnabled = false

    constructor(@NonNull context: Context) : super(context) {}
    constructor(@NonNull context: Context, @Nullable attrs: AttributeSet?) : super(context, attrs) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasureSpec = heightMeasureSpec
        if (currentPosition < childCount) {
            val child: View = getChildAt(currentPosition)
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
            val height: Int = child.getMeasuredHeight()
            if (height != 0) {
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return when (swipeEnabled) {
            true -> super.onInterceptTouchEvent(event)
            false -> false
        }
    }

    fun setSwipePagingEnabled(swipeEnabled: Boolean) {
        this.swipeEnabled = swipeEnabled
    }

    fun measureViewPager(currentPosition: Int) {
        this.currentPosition = currentPosition
        requestLayout()
    }
}