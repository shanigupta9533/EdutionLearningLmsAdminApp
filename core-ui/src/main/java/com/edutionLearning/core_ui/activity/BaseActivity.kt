package com.edutionLearning.core_ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.edutionLearning.core_ui.R

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
    }
}