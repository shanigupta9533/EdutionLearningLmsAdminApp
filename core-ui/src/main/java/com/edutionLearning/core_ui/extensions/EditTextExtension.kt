package com.edutionLearning.core_ui.extensions

import android.annotation.SuppressLint
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import com.edutionLearning.core.type.EMPTY
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

fun EditText.onTextChangeFlow(): StateFlow<String> {
    val flow = MutableStateFlow(String.EMPTY)
    this.doOnTextChanged { newText: CharSequence?, _: Int, _: Int, _: Int -> flow.value = newText.toString() }
    return flow.asStateFlow()
}

fun EditText.showKeyboard() {
    if (requestFocus()) {
        (this.context?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
            .showSoftInput(this, SHOW_IMPLICIT)
        setSelection(text.length)
    }
}

/**
 * Use this function to set the clickListener for any rightDrawable used in the EditText
 */
@SuppressLint("ClickableViewAccessibility")
fun EditText.setDrawableRightListener(listener: () -> Unit) {
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