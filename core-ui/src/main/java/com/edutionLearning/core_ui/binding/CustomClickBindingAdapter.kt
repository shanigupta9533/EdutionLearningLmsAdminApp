package com.edutionLearning.core_ui.binding

import android.os.SystemClock
import android.view.View
import androidx.databinding.BindingAdapter

object CustomClickBindingAdapter {
    @JvmStatic
    @BindingAdapter("onClickDebounce")
    fun onClickDebounce(view: View, listener: View.OnClickListener) {
        view.setClickDebounce {
            listener.onClick(view)
        }
    }

    object LastClickTime {
        var lastClickWithTime: Long = 0
    }

    private fun View.setClickDebounce(action: () -> Unit) {
        setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                if (SystemClock.elapsedRealtime() - LastClickTime.lastClickWithTime < 1000L) return
                else action()
                LastClickTime.lastClickWithTime = SystemClock.elapsedRealtime()
            }
        })
    }

}