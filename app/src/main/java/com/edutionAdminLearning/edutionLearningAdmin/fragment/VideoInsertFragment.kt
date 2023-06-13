package com.edutionAdminLearning.edutionLearningAdmin.fragment

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.edutionAdminLearning.core.result.ApiResponse
import com.edutionAdminLearning.core.type.EMPTY
import com.edutionAdminLearning.core.type.value
import com.edutionAdminLearning.core_ui.extensions.toastL
import com.edutionAdminLearning.core_ui.fragment.ViewModelBindingFragment
import com.edutionAdminLearning.edutionLearningAdmin.R
import com.edutionAdminLearning.edutionLearningAdmin.databinding.FragmentVideoInsertBinding
import com.edutionAdminLearning.edutionLearningAdmin.utils.Constants
import com.edutionAdminLearning.edutionLearningAdmin.utils.Constants.VIDEO_UPLOAD_MODULE_KEY
import com.edutionAdminLearning.edutionLearningAdmin.utils.FilePickUtils
import com.edutionAdminLearning.edutionLearningAdmin.utils.VIDEO_EXTENSIONS
import com.edutionAdminLearning.edutionLearningAdmin.utils.makeStoragePermission
import com.edutionAdminLearning.edutionLearningAdmin.utils.setVideoUploadFile
import com.edutionAdminLearning.edutionLearningAdmin.viewmodel.HomeDetailsViewModel
import com.edutionAdminLearning.network.utils.FileUploaderService
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.gotev.uploadservice.data.UploadInfo
import net.gotev.uploadservice.exceptions.UploadError
import net.gotev.uploadservice.exceptions.UserCancelledUploadException
import net.gotev.uploadservice.network.ServerResponse
import net.gotev.uploadservice.observer.request.RequestObserver
import net.gotev.uploadservice.observer.request.RequestObserverDelegate
import java.io.File
import java.lang.Exception
import javax.inject.Inject

@AndroidEntryPoint
class VideoInsertFragment : ViewModelBindingFragment<FragmentVideoInsertBinding, HomeDetailsViewModel>(
    FragmentVideoInsertBinding::inflate
), RequestObserverDelegate {

    @Inject
    lateinit var fileUploaderService: FileUploaderService
    override val viewModel: HomeDetailsViewModel by viewModels()
    private var videoFile: File? = null

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

    // after choosing file from storage
    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {

                viewModel.startLoading()
                viewLifecycleScope?.launch {
                    result.data?.data?.let {
                        context?.apply {

                            FilePickUtils.getFile(this, it, { file ->

                                viewModel.stopLoading()
                                val fileInMb = file.length() / (1024.0f * 1024.0f)
                                if (fileInMb > 1000) {
                                    toastL(getString(R.string.video_size_should_be_less_than_1gb))
                                    return@getFile
                                }

                                binding.apply {
                                    viewLifecycleScope?.launch(Dispatchers.Main) {
                                        videoImageParent.visibility = View.VISIBLE
                                        fileUrl = file.path
                                        executePendingBindings()
                                        videoFile = file
                                    }
                                }

                            }) { error ->
                                Log.i("TAG", "something went wrong: $error")
                            }
                        }
                    }
                }
            }
        }

    fun chooseFile() {
        requireActivity().makeStoragePermission(VIDEO_EXTENSIONS, startForResult)
    }

    fun cancelImage() {
        binding.videoImageParent.visibility = View.GONE
        videoFile = null
    }

    fun uploadDocument() {

        binding.apply {

            videoNameEt.error = null
            when {

                videoNameEt.text.toString().length < 3 -> {
                    videoNameEt.error = getString(R.string.min_3_letter_required_in_course_type_field)
                    videoNameEt.requestFocus()
                }

                (videoFile == null || videoFile?.exists()?.not() == true) -> {
                    toastL(getString(R.string.choose_a_video))
                }

                else -> {
                    viewModel.startLoading()
                    context?.apply {
                        setVideoUploadFile(
                            data = videoFile,
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
            videoFile = null
            videoNameEt.setText(String.EMPTY)
            videoImageParent.visibility = View.GONE
            fileUrl = String.EMPTY
            executePendingBindings()
        }

        toastL(getString(R.string.upload_successfully))
    }
}