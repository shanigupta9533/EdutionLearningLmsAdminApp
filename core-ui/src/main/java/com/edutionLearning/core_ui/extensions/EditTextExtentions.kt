package com.edutionLearning.core_ui.extensions

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.edutionLearning.core.type.EMPTY
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

fun EditText.searchQuery(): StateFlow<String> {
    val queryFlow = MutableStateFlow(String.EMPTY)
    this.addTextChangedListener(object : TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            queryFlow.tryEmit(s.toString())
        }

        override fun afterTextChanged(s: Editable?) {
        }
    })

    return queryFlow.asStateFlow()
}

fun EditText.clearText() {
    this.text.clear()
}