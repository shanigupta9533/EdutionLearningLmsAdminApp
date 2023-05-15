package com.edutionAdminLearning.core_ui.fragment

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

abstract class BaseDialogFragment : DialogFragment() {

    open fun onBack() {}

    open fun onBackPressed(): Boolean {
        return false
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(requireActivity(), theme) {
            override fun onBackPressed() {
                if (this@BaseDialogFragment.onBackPressed())
                    super.onBackPressed()
            }
        }
    }

}