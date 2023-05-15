package com.edutionLearning.core_ui.edutionWidget

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.core.content.res.ResourcesCompat
import com.edutionLearning.core_ui.R
import com.google.android.material.tabs.TabLayout

class FontTabLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, @AttrRes defStyleAttr: Int = 0
) : TabLayout(context, attrs, defStyleAttr) {

    private var defaultSelectedPosition = 0

    private var selectedTypeFace: Typeface? = ResourcesCompat.getFont(context, R.font.gilroy_semi_bold)
    private var normalTypeFace: Typeface? = ResourcesCompat.getFont(context, R.font.gilroy_regular)

    init {
        attrs?.let { initAttrs(it) }
        addOnTabSelectedListener()
    }

    private fun initAttrs(attrs: AttributeSet) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.FontTabLayout)


        defaultSelectedPosition = a.getInteger(R.styleable.FontTabLayout_defaultSelectedPosition, 0)
        val selectedResourceId = a.getResourceId(R.styleable.FontTabLayout_selectedTypeFace, R.font.gilroy_bold)
        val normalResourceId = a.getResourceId(R.styleable.FontTabLayout_normalTypeFace, R.font.gilroy_semi_bold)

        selectedTypeFace = ResourcesCompat.getFont(context, selectedResourceId)
        normalTypeFace = ResourcesCompat.getFont(context, normalResourceId)

        a.recycle()
    }

    private fun addOnTabSelectedListener() {
        addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabUnselected(tab: Tab) {
                getCustomViewFromTab(tab).apply { typeface = normalTypeFace }
            }

            override fun onTabReselected(tab: Tab) {
            }

            override fun onTabSelected(tab: Tab) {
                getCustomViewFromTab(tab).apply { typeface = selectedTypeFace }
            }

            private fun getCustomViewFromTab(tab: Tab): TextView {
                val linearLayout = (getChildAt(0) as ViewGroup).getChildAt(tab.position) as LinearLayout
                return linearLayout.getChildAt(1) as TextView
            }
        })
    }
}