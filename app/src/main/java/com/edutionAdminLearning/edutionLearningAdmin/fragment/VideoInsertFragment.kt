package com.edutionAdminLearning.edutionLearningAdmin.fragment

import android.content.Context
import android.util.Log
import androidx.fragment.app.viewModels
import com.edutionAdminLearning.core.result.ApiResponse
import com.edutionAdminLearning.core.type.EMPTY
import com.edutionAdminLearning.core_ui.extensions.toastL
import com.edutionAdminLearning.core_ui.fragment.ViewModelBindingFragment
import com.edutionAdminLearning.edutionLearningAdmin.R
import com.edutionAdminLearning.edutionLearningAdmin.databinding.FragmentVideoInsertBinding
import com.edutionAdminLearning.edutionLearningAdmin.utils.Constants
import com.edutionAdminLearning.edutionLearningAdmin.utils.setVideoUploadFile
import com.edutionAdminLearning.edutionLearningAdmin.viewmodel.HomeDetailsViewModel
import com.edutionAdminLearning.network.utils.FileUploaderService
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import net.gotev.uploadservice.data.UploadInfo
import net.gotev.uploadservice.exceptions.UploadError
import net.gotev.uploadservice.exceptions.UserCancelledUploadException
import net.gotev.uploadservice.network.ServerResponse
import net.gotev.uploadservice.observer.request.RequestObserver
import net.gotev.uploadservice.observer.request.RequestObserverDelegate
import java.lang.Exception
import javax.inject.Inject

@AndroidEntryPoint
class VideoInsertFragment : ViewModelBindingFragment<FragmentVideoInsertBinding, HomeDetailsViewModel>(
    FragmentVideoInsertBinding::inflate
), RequestObserverDelegate {

    @Inject
    lateinit var fileUploaderService: FileUploaderService
    override val viewModel: HomeDetailsViewModel by viewModels()

    override fun FragmentVideoInsertBinding.setViewBindingVariables() {
        toolbarText = getString(R.string.video_details_insert)
        vm = viewModel
        controller = this@VideoInsertFragment
    }

    override fun FragmentVideoInsertBinding.setViewModelBindingData() {
        lifecycleOwner = viewLifecycleOwner

        toolbar.appCompatImageView.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        RequestObserver(requireContext(),
            requireActivity(),
            this@VideoInsertFragment,
            shouldAcceptEventsFrom = { uploadInfo ->
                uploadInfo.uploadId.startsWith(
                    Constants.VIDEO_UPLOAD_MODULE_KEY
                )
            })

    }

    fun uploadDocument() {

        binding.apply {

            videoNameEt.error = null
            videoUrl.error = null
            videoEmbedUrl.error = null
            when {

                videoNameEt.text.toString().length < 3 -> {
                    videoNameEt.error = getString(R.string.min_3_letter_required_in_course_type_field)
                    videoNameEt.requestFocus()
                }

                videoUrl.text.toString().length < 3 -> {
                    videoUrl.error = getString(R.string.min_3_letter_required_in_course_type_field)
                    videoUrl.requestFocus()
                }

                videoEmbedUrl.text.toString().length < 3 -> {
                    videoEmbedUrl.error = getString(R.string.min_3_letter_required_in_course_type_field)
                    videoEmbedUrl.requestFocus()
                }

                else -> {
                    viewModel.startLoading()
                    context?.apply {
                        setVideoUploadFile(
                            videoUrl = binding.videoUrl.text.toString(),
                            videoEmbedUrl = binding.videoEmbedUrl.text.toString(),
                            messageText = binding.videoNameEt.text.toString(),
                            fileUploaderService = fileUploaderService,
                        )
                    }
                }
            }
        }
    }

    override fun onBackPressed(): Boolean {
        return false
    }

    override fun onCompleted(context: Context, uploadInfo: UploadInfo) {

    }

    override fun onCompletedWhileNotObserving() {

    }

    override fun onError(context: Context, uploadInfo: UploadInfo, exception: Throwable) {
        viewModel.stopLoading()

        when (exception) {
            is UserCancelledUploadException -> {
                Log.e("RECEIVER", "Error, user cancelled upload: $uploadInfo")
            }

            is UploadError -> {

                try {
                    val response = Gson().fromJson(exception.serverResponse.bodyString, ApiResponse::class.java)
                    toastL(response.error?.message)
                    Log.e("RECEIVER", "Error, upload error: ${exception.serverResponse.bodyString}")
                } catch (e: Exception){
                    e.message
                }
            }

            else -> {
                Log.e("RECEIVER", "Error: $uploadInfo", exception)
            }
        }

    }

    override fun onProgress(context: Context, uploadInfo: UploadInfo) {
    }

    override fun onSuccess(context: Context, uploadInfo: UploadInfo, serverResponse: ServerResponse) {
        viewModel.stopLoading()

        binding.apply {
            videoNameEt.setText(String.EMPTY)
            videoEmbedUrl.setText(String.EMPTY)
            videoUrl.setText(String.EMPTY)
            executePendingBindings()
        }

        toastL(getString(R.string.upload_successfully))
    }
}