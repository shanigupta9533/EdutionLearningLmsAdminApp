package com.edutionAdminLearning.core_ui.extensions

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.util.TypedValue
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.window.OnBackInvokedDispatcher
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.BuildCompat
import androidx.fragment.app.FragmentActivity

/**
 * Use this method to check keyboard status from any activity
 */
fun Activity.isKeyboardVisible(): Boolean {
    val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    val windowHeightMethod = InputMethodManager::class.java.getMethod("getInputMethodWindowVisibleHeight")
    val height = (windowHeightMethod.invoke(imm) as? Int) ?: 0
    return height > 0
}

fun Activity?.keepScreenON() {
    if (this == null) return
    this.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
}

fun Activity.getDeviceWidth(): Int {
    return try {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) windowManager.currentWindowMetrics.bounds.width()
        else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) display?.width ?: 0
        else windowManager?.defaultDisplay?.width ?: 0
    } catch (e: Exception) {
        0
    }

}

fun Activity?.getScreenWidthDP() = this?.resources?.configuration?.screenWidthDp ?: 0

fun Activity.getColorInt(action: () -> Int) = ContextCompat.getColor(this, action())

fun FragmentActivity.isPortrait() = this.resources?.configuration?.orientation == Configuration.ORIENTATION_PORTRAIT
fun FragmentActivity.isLandscape() = this.resources?.configuration?.orientation == Configuration.ORIENTATION_LANDSCAPE

@SuppressLint("UnsafeOptInUsageError")
fun AppCompatActivity.addOnBackPressedDispatcher(onBackPressed: (() -> Unit)? = null) {
    if (BuildCompat.isAtLeastT()) onBackInvokedDispatcher.registerOnBackInvokedCallback(OnBackInvokedDispatcher.PRIORITY_DEFAULT) { onBackPressed?.invoke() }
    else onBackPressedDispatcher.addCallback(this) { onBackPressed?.invoke() }
}

fun Activity.convertToDp(value: Float): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, resources.displayMetrics).toInt()
}