package com.edutionLearning.core_ui.extensions

import android.content.res.Configuration
import android.graphics.Color
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController

fun Fragment.navigateWhenDestinationIs(@IdRes id: Int, perform: NavController.() -> Unit) =
    with(findNavController()) {
        if (currentDestination?.id == id)
            perform()
    }

fun Fragment.getColorInt(action: () -> Int): Int {
    return context?.let {
        ContextCompat.getColor(it, action())
    } ?: Color.WHITE
}


/** Specifies portrait-orientation of Screen **/
fun Fragment.isPortrait() = activity?.resources?.configuration?.orientation == Configuration.ORIENTATION_PORTRAIT

/** Specifies landscape-orientation of Screen **/
fun Fragment.isLandscape() = activity?.resources?.configuration?.orientation == Configuration.ORIENTATION_LANDSCAPE

/** Calculates screen_width in dp **/
fun Fragment.screenWidthDP() = activity?.resources?.configuration?.screenWidthDp ?: 0
