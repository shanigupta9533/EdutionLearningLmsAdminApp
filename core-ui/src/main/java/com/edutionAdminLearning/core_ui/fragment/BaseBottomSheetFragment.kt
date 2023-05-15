package com.edutionAdminLearning.core_ui.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetFragment : BottomSheetDialogFragment() {

    open fun onBack() {}

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {
            val parentLayout =
                (it as? BottomSheetDialog)?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                    ?: return@setOnShowListener
            with(parentLayout) {
                if (isFullScreen()) setupFullHeight(this)
                BottomSheetBehavior.from(this)
                    .apply { if (isExpanded()) state = BottomSheetBehavior.STATE_EXPANDED }
            }
        }
        return dialog
    }

    open fun isFullScreen(): Boolean {
        return false
    }

    open fun isExpanded(): Boolean {
        return false
    }

    private fun setupFullHeight(bottomSheet: View) {
        bottomSheet.layoutParams.apply {
            height = WindowManager.LayoutParams.MATCH_PARENT
            bottomSheet.layoutParams = this
        }
    }
}