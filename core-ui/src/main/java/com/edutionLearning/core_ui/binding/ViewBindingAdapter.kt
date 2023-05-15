package com.edutionLearning.core_ui.binding

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.EditText
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import com.edutionLearning.core.type.EMPTY
import com.edutionLearning.core_ui.R
import com.edutionLearning.core_ui.extensions.hideKeyboard
import com.facebook.shimmer.ShimmerFrameLayout


private const val MIME_HTML = "text/html"
private const val ENCODING_UTF_8 = "UTF-8"


@BindingAdapter("hideKeyboardOnFocus")
fun EditText.hideKeyboardOnFocus(hide: Boolean) {
    setOnClickListener {
        if (hide) {
            hideKeyboard()
        }
    }
}

@BindingAdapter("htmlText")
fun TextView.htmlText(htmlText: String?) {
    text = HtmlCompat.fromHtml(htmlText ?: String.EMPTY, HtmlCompat.FROM_HTML_MODE_COMPACT)
}

@BindingAdapter("fadeInVisible")
fun TextView.fadeInVisible(visible: Boolean = false) {
   val animation = if(visible){
        visibility = View.VISIBLE
        AnimationUtils.loadAnimation(context, R.anim.fade_in)
    } else {
        visibility = View.GONE
        AnimationUtils.loadAnimation(context, R.anim.fade_out)
    }
    startAnimation(animation)
}

@SuppressLint("ClickableViewAccessibility")
@BindingAdapter("htmlText")
fun WebView.htmlText(htmlText: String?) {
    htmlText?.let {
        loadDataWithBaseURL(null, it, MIME_HTML, ENCODING_UTF_8, null)
        settings.textSize = WebSettings.TextSize.NORMAL
        setOnTouchListener { _, event -> event.action == MotionEvent.ACTION_MOVE }
    }
}

@BindingAdapter("goneUnless")
fun View.goneUnless(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}


@BindingAdapter("auto_start")
fun ShimmerFrameLayout.autoStart(visible: Boolean) {
      if (visible) this.startShimmer() else this.stopShimmer()
}

@BindingAdapter("invisibleUnless")
fun View.invisibleUnless(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("strikeThrough")
fun TextView.showStrikeThrough(show: Boolean) {
    paintFlags =
        if (show) paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        else paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
}

