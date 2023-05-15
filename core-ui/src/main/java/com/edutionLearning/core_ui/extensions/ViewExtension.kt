package com.edutionLearning.core_ui.extensions

import android.app.Activity
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Patterns
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.viewbinding.BuildConfig
import com.edutionLearning.core_ui.BR


/**
 * Use this method to hide keyboard from any view
 */
fun View.hideKeyboard() {
    val inputMethodManager = context.getSystemService(
        Activity.INPUT_METHOD_SERVICE
    ) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(
        windowToken, 0
    )
}

/**
 * Use this method to show keyboard from any view
 */
fun View.showKeyboard() {
    val inputMethodManager = context.getSystemService(
        Activity.INPUT_METHOD_SERVICE
    ) as InputMethodManager
    inputMethodManager.showSoftInput(
        this, InputMethodManager.SHOW_IMPLICIT
    )
}

fun EditText.isEmpty(): Boolean {
    return this.text.toString().isEmpty()
}

fun EditText.isContainsLink(): Boolean {
    return Patterns.WEB_URL.matcher(this.text.toString().lowercase()).matches();
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.isVisible(isVisible: Boolean) {
    this.isVisible = isVisible
}

fun Activity.setStatusBarColorAndAppearance(statusBarColor: Int, isLight: Boolean ) {
    try {
        window.statusBarColor = ContextCompat.getColor(this, statusBarColor)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = isLight
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
fun FragmentActivity.disableScreenShot() {
    if (BuildConfig.DEBUG) {
        return
    }
    this.window.setFlags(
        WindowManager.LayoutParams.FLAG_SECURE,
        WindowManager.LayoutParams.FLAG_SECURE
    )
}

fun Context.dp(intValue: Int) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP, intValue.toFloat(),
    this.resources.displayMetrics
).toInt()

@JvmOverloads
fun TextView.setMultiColorText(textOne: String, colorOne: Int, textTwo: String? = null, colorTwo: Int? = null, textThree: String? = null, colorThree: Int? = null) {
    val word: Spannable = SpannableString(textOne)
    word.setSpan(
        ForegroundColorSpan(ContextCompat.getColor(this.context, colorOne)),
        0,
        word.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    this.text = word

    safeLet(textTwo, colorTwo) {text, color ->
        val wordTwo: Spannable = SpannableString(text)
        wordTwo.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this.context, color)),
            0,
            wordTwo.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        this.append(wordTwo)
    }

    safeLet(textThree, colorThree) {text, color ->
        val wordTwo: Spannable = SpannableString(text)
        wordTwo.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this.context, color)),
            0,
            wordTwo.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        this.append(wordTwo)
    }

}

fun View.changeBackgroundStrokeColor(color: Int) {
    (this.background.mutate() as GradientDrawable).setStroke(
        this.context.dp(1),
        ContextCompat.getColor(this.context, color)
    )
}

fun View.changeDashedBackgroundStrokeColor(color: Int, dashWidth: Int, dashGap: Int) {
    (this.background.mutate() as GradientDrawable).setStroke(
        this.context.dp(1),
        ContextCompat.getColor(this.context, color),
        this.context.dp(dashWidth).toFloat(),
        this.context.dp(dashGap).toFloat()
    )
}

inline fun <T1 : Any, T2 : Any, R : Any> safeLet(p1: T1?, p2: T2?, block: (T1, T2) -> R?): R? {
    return if (p1 != null && p2 != null) block(p1, p2) else null
}