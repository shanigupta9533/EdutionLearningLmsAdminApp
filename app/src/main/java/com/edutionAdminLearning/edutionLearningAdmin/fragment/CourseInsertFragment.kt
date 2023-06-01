package com.edutionAdminLearning.edutionLearningAdmin.fragment

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.edutionAdminLearning.core.result.ApiResponse
import com.edutionAdminLearning.core.type.EMPTY
import com.edutionAdminLearning.core.type.value
import com.edutionAdminLearning.core_ui.extensions.toastL
import com.edutionAdminLearning.core_ui.fragment.ViewModelBindingFragment
import com.edutionAdminLearning.edutionLearningAdmin.R
import com.edutionAdminLearning.edutionLearningAdmin.databinding.FragmentCourseInsertBinding
import com.edutionAdminLearning.edutionLearningAdmin.utils.Constants
import com.edutionAdminLearning.edutionLearningAdmin.utils.FilePickUtils
import com.edutionAdminLearning.edutionLearningAdmin.utils.IMAGE_EXTENSIONS
import com.edutionAdminLearning.edutionLearningAdmin.utils.makeStoragePermission
import com.edutionAdminLearning.edutionLearningAdmin.utils.setBannerUploadFile
import com.edutionAdminLearning.edutionLearningAdmin.utils.setCourseUploadFile
import com.edutionAdminLearning.edutionLearningAdmin.utils.updateBannerUploadFile
import com.edutionAdminLearning.edutionLearningAdmin.utils.updateCourseUploadFile
import com.edutionAdminLearning.edutionLearningAdmin.viewmodel.CourseDetailsViewModel
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
import javax.inject.Inject

@AndroidEntryPoint
class CourseInsertFragment : ViewModelBindingFragment<FragmentCourseInsertBinding, CourseDetailsViewModel>(
    FragmentCourseInsertBinding::inflate
), RequestObserverDelegate {

    @Inject
    lateinit var fileUploaderService: FileUploaderService

    private val args by navArgs<CourseInsertFragmentArgs>()
    override val viewModel: CourseDetailsViewModel by viewModels()
    private var courseFile: File? = null

    override fun FragmentCourseInsertBinding.setViewBindingVariables() {
        toolbarText = getString(R.string.insert_course_details)
        vm = viewModel
        controller = this@CourseInsertFragment
    }

    override fun FragmentCourseInsertBinding.setViewModelBindingData() {
        lifecycleOwner = viewLifecycleOwner

        if (args.isUpdate) {
            args.data?.let {
                courseName.setText(it.courseName)
                courseBasicPrice.setText(it.coursePriceDetails)
                courseDesc.setText(it.courseDesc)
                submit.text = getString(R.string.update_course_details)
                fileUrl = it.courseImage
                courseImageParent.visibility = View.VISIBLE
            }
        }

        toolbar.appCompatImageView.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        RequestObserver(requireContext(),
            requireActivity(),
            this@CourseInsertFragment,
            shouldAcceptEventsFrom = { uploadInfo ->
                uploadInfo.uploadId.startsWith(
                    if (args.isUpdate)
                        Constants.COURSE_UPLOAD_UPDATE_MODULE_KEY
                    else
                        Constants.COURSE_UPLOAD_MODULE_KEY
                )
            })

    }

    fun chooseFile() {
        requireActivity().makeStoragePermission(IMAGE_EXTENSIONS, startForResult)
    }

    fun cancelImage() {
        binding.courseImageParent.visibility = View.GONE
        courseFile = null
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
                                if (fileInMb > 1) {
                                    toastL(getString(R.string.image_size_should_be_less_than_1mb))
                                    return@getFile
                                }

                                binding.apply {
                                    viewLifecycleScope?.launch(Dispatchers.Main) {
                                        courseImageParent.visibility = View.VISIBLE
                                        fileUrl = file.path
                                        executePendingBindings()
                                        courseFile = file
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

    fun uploadDocument() {

        binding.apply {

            courseName.error = null
            courseDesc.error = null
            courseBasicPrice.error = null
            when {

                courseName.text.toString().length < 3 -> {
                    courseName.error = getString(R.string.min_3_letter_required_in_course_type_field)
                    courseName.requestFocus()
                }

                courseDesc.text.toString().length < 10 -> {
                    courseDesc.error = getString(R.string.min_10_letter_required_in_course_type_field)
                    courseDesc.requestFocus()
                }

                courseBasicPrice.text.toString().isEmpty() -> {
                    courseBasicPrice.error = getString(R.string.enter_starting_price)
                    courseBasicPrice.requestFocus()
                }

                (courseFile == null || courseFile?.exists()?.not() == true) && args.isUpdate.not() -> {
                    toastL(getString(R.string.choose_a_image))
                }

                args.isUpdate -> {
                    viewModel.startLoading()
                    updateCourseUploadFile(
                        data = courseFile,
                        courseName = binding.courseName.text.toString(),
                        courseDesc = binding.courseDesc.text.toString(),
                        coursePrice = binding.courseBasicPrice.text.toString(),
                        fileUploaderService = fileUploaderService,
                        courseId = args.data?.id.value
                    )
                }

                else -> {
                    viewModel.startLoading()
                    context?.apply {
                        setCourseUploadFile(
                            data = courseFile,
                            courseName = binding.courseName.text.toString(),
                            courseDesc = binding.courseDesc.text.toString(),
                            coursePrice = binding.courseBasicPrice.text.toString(),
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

                val response = Gson().fromJson(exception.serverResponse.bodyString, ApiResponse::class.java)
                toastL(response.error?.message)
                Log.e("RECEIVER", "Error, upload error: ${exception.serverResponse.bodyString}")
            }

            else -> {
                Log.e("RECEIVER", "Error: $uploadInfo", exception)
            }
        }

    }

    override fun onProgress(context: Context, uploadInfo: UploadInfo) {
        if(uploadInfo.progressPercent == 2){
            viewModel.startLoading()
        }
    }

    override fun onSuccess(context: Context, uploadInfo: UploadInfo, serverResponse: ServerResponse) {
        viewModel.stopLoading()

        if (args.isUpdate.not())
            binding.apply {
                courseFile = null
                courseName.setText(String.EMPTY)
                courseDesc.setText(String.EMPTY)
                courseBasicPrice.setText(String.EMPTY)
                courseImageParent.visibility = View.GONE
                fileUrl = String.EMPTY
                executePendingBindings()
            }

        toastL(getString(R.string.upload_successfully))

    }

}