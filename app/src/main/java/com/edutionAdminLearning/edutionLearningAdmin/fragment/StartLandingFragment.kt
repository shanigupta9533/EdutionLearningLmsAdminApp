package com.edutionAdminLearning.edutionLearningAdmin.fragment

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.edutionAdminLearning.core_ui.fragment.ViewBindingFragment
import com.edutionAdminLearning.edutionLearningAdmin.databinding.FragmentStartLandingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartLandingFragment : ViewBindingFragment<FragmentStartLandingBinding>(
    FragmentStartLandingBinding::inflate
) {

    private val args by navArgs<StartLandingFragmentArgs>()
    override fun FragmentStartLandingBinding.setViewBindingVariables() {

    }

    override fun FragmentStartLandingBinding.setViewBindingData() {

        when (args.fwdLocation) {
            FWD_LOCATION_COURSES -> {
                findNavController().navigate(
                    StartLandingFragmentDirections.goToCourses()
                )
            }

            FWD_LOCATION_BANNERS -> {
                findNavController().navigate(
                    StartLandingFragmentDirections.goToBanners()
                )
            }

            FWD_LOCATION_NOTIFICATION -> {
                findNavController().navigate(
                    StartLandingFragmentDirections.goToNotification()
                )
            }

            FWD_LOCATION_ADMIN_ACCESS -> {
                findNavController().navigate(
                    StartLandingFragmentDirections.goToAdminUsers()
                )
            }

            FWD_LOCATION_USER_DETAILS -> {

            }
        }

    }

    override fun onBackPressed(): Boolean {
        return false
    }

    companion object {
        const val FWD_LOCATION_COURSES = "SARTHI_COURSES"
        const val FWD_LOCATION_BANNERS = "SARTHI_BANNERS"
        const val FWD_LOCATION_NOTIFICATION = "SARTHI_NOTIFICATIONS"
        const val FWD_LOCATION_ADMIN_ACCESS = "SARTHI_ADMIN_ACCESS"
        const val FWD_LOCATION_USER_DETAILS = "SARTHI_USERS_DETAILS"
    }

}