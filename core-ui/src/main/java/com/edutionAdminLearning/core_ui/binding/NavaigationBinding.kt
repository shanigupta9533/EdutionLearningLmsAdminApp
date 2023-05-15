package com.edutionAdminLearning.core_ui.binding

import android.os.Build
import android.transition.TransitionInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigatorExtras

fun NavController.navigateSharedTransition(directions: NavDirections, vararg sharedElements: Pair<View, String>) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val extras = FragmentNavigatorExtras(
            *sharedElements
        )
        navigate(directions, extras)
    } else {
        navigate(directions)
    }
}

fun Fragment.sharedTransitionMove() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }
}

fun Fragment.sharedTransitionSlideRight() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.slide_right)
    }
}

fun Fragment.sharedTransitionSlideLef() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.slide_left)
    }
}