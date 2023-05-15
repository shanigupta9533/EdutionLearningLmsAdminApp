package com.edutionAdminLearning.core_ui.view.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.TextView
import com.edutionAdminLearning.core_ui.R

class ProgressCustomDialog {
    companion object {
        fun progressDialog(context: Context, title: String?): Dialog {
            val dialog = Dialog(context)
            val inflate = LayoutInflater.from(context).inflate(R.layout.progress_dialog, null)
            dialog.setContentView(inflate)
            title?.let { inflate.findViewById<TextView>(R.id.title_txt)?.text = it }
            dialog.setCancelable(false)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            return dialog
        }
    }
}