package com.edutionLearning.core_ui.extensions

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.core.text.HtmlCompat
import com.edutionLearning.core.type.EMPTY
import com.google.android.material.textfield.MaterialAutoCompleteTextView

/**
 * Use this extension function to handle the item clicked. and provide the value that you want to
 * show on autocomplete edit text from your data model.
 */
fun <T> MaterialAutoCompleteTextView.onItemClick(onClick: (item: T) -> String) {
    setOnItemClickListener { adapterView, _, i, _ ->
        val item = adapterView.getItemAtPosition(i) as T
        setText(onClick(item))
    }
}

/**
 * Use this function to set HTML Text to any textView.
 * Pass the tag according to your requirement
 * Basic tags are HtmlCompat.FROM_HTML_MODE_COMPACT , HtmlCompat.FROM_HTML_OPTION_USE_CSS_COLORS ... etc.
 */
fun TextView.setHTMLText(
    @Nullable htmlString: String?,
    tag: Int = HtmlCompat.FROM_HTML_MODE_COMPACT
) {
    this.text = htmlString?.let { HtmlCompat.fromHtml(it, tag) } ?: String.EMPTY
}

/**
 * Use this function to set the clickListener for any rightDrawable used in the TextView
 */
@SuppressLint("ClickableViewAccessibility")
fun TextView.setDrawableRightListener(listener: () -> Unit) {
    isClickable = true
    setOnTouchListener { _, motionEvent ->
        val drawableRight = 2
        if (motionEvent.action != MotionEvent.ACTION_UP || compoundDrawables[drawableRight] == null)
            return@setOnTouchListener false
        if (motionEvent.rawX >= (right - compoundDrawables[drawableRight].bounds.width())) {
            listener()
            return@setOnTouchListener false
        }
        false
    }
}
