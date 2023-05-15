package com.edutionLearning.core_ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.edutionLearning.core_ui.R
import com.google.android.material.appbar.MaterialToolbar

abstract class BaseViewBindingActivity<VB : ViewBinding>(layoutId: (LayoutInflater) -> VB) : ViewBindingActivity<VB>(layoutId) {

    override fun onCreate(savedInstanceState: Bundle?) {
        getThemeCustom()?.let { setTheme(it) }
        super.onCreate(savedInstanceState)
    }

    override fun VB.setViewToolbar() {
        getToolbar()?.let { toolBar ->
            setSupportActionBar(toolBar)
            supportActionBar?.apply {
                setDisplayShowTitleEnabled(false)
                if (isToolBarHide()) hide()
            }
            toolBar.title = setToolbarTitle()
            toolBar.setTitleTextColor(ContextCompat.getColor(this@BaseViewBindingActivity, setToolbarTitleColor()))
            toolBar.isTitleCentered = isToolbarTitleCenter()
        }
    }

    open fun getThemeCustom(): Int? {
        return null
    }

    open fun  getToolbar(): MaterialToolbar?{
        return null
    }

    open fun isToolBarHide(): Boolean {
        return false
    }

    open fun setToolbarTitle(): String {
        return getString(R.string.app_name)
    }

    open fun setToolbarTitleColor(): Int {
        return R.color.white
    }


    open fun isToolbarTitleCenter(): Boolean {
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}