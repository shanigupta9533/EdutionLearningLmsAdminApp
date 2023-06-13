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
import com.edutionAdminLearning.edutionLearningAdmin.databinding.FragmentCourseVideosInsertBinding
import com.edutionAdminLearning.edutionLearningAdmin.di.ChatEvents
import com.edutionAdminLearning.edutionLearningAdmin.utils.Constants
import com.edutionAdminLearning.edutionLearningAdmin.utils.FilePickUtils
import com.edutionAdminLearning.edutionLearningAdmin.utils.makeStoragePermission
import com.edutionAdminLearning.edutionLearningAdmin.utils.mimeTypesOfEverything
import com.edutionAdminLearning.edutionLearningAdmin.utils.mimeTypesOfPdf
import com.edutionAdminLearning.edutionLearningAdmin.utils.setCourseVideoUploadFile
import com.edutionAdminLearning.edutionLearningAdmin.utils.updateCourseVideoUploadFile
import com.edutionAdminLearning.edutionLearningAdmin.viewmodel.CourseDetailsViewModel
import com.edutionAdminLearning.network.utils.FileUploaderService
import com.edutionAdminLearning.network.utils.FileUtils
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.gotev.uploadservice.data.UploadInfo
import net.gotev.uploadservice.exceptions.UploadError
import net.gotev.uploadservice.exceptions.UserCancelledUploadException
import net.gotev.uploadservice.network.ServerResponse
import net.gotev.uploadservice.observer.request.RequestObserver
import net.gotev.uploadservice.observer.request.RequestObserverDelegate
import java.io.File
import javax.inject.Inject

data class VideoDetails(
    val videoId: String,
    val videoName: String
)

@AndroidEntryPoint
class CourseVideosInsertFragment : ViewModelBindingFragment<FragmentCourseVideosInsertBinding, CourseDetailsViewModel>(
    FragmentCourseVideosInsertBinding::inflate
), RequestObserverDelegate {

    private val args by navArgs<CourseVideosInsertFragmentArgs>()
    private var uploadType: UploadType = UploadType.COURSE_CODE_LINK
    override val viewModel: CourseDetailsViewModel by viewModels()
    private var homeWorkFile: File? = null
    private var projectFile: File? = null
    private var codeFile: File? = null
    private var videoData: VideoDetails? = null

    @Inject
    lateinit var fileUploaderService: FileUploaderService

    override fun FragmentCourseVideosInsertBinding.setViewBindingVariables() {
        toolbarText = getString(R.string.course_videos_insert)
        vm = viewModel
        controller = this@CourseVideosInsertFragment
    }

    override fun FragmentCourseVideosInsertBinding.setViewModelBindingData() {
        lifecycleOwner = viewLifecycleOwner
        toolbar.appCompatImageView.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        if (args.isUpdate) {
            args.data?.let {
                lectureName.setText(it.lectureName.value)
                submit.text = getString(R.string.update_course_video_details)
                displayOrder.visibility = View.VISIBLE
                displayOrder.setText(args.data?.displayOrder.value)

                videoData = VideoDetails(
                    videoId = it.videoId,
                    videoName = it.videoName,
                )

                videoNameEt.post {
                    videoNameEt.setText(it.videoName)
                }

            }
        }

        viewLifecycleScope?.launch {
            ChatEvents.onVideoEvents.collect {

                it?.let {

                    videoData = VideoDetails(
                        videoId = it.id.value,
                        videoName = it.videoName.value
                    )

                    videoNameEt.post {
                        videoNameEt.setText(it.videoName.value)
                    }

                    ChatEvents.onVideoSelected(null)
                }

            }
        }

        videoNameEt.setOnClickListener {
            findNavController().navigate(
                CourseVideosInsertFragmentDirections.goToVideo(
                    isFromCourse = true
                )
            )
        }

        RequestObserver(requireContext(),
            requireActivity(),
            this@CourseVideosInsertFragment,
            shouldAcceptEventsFrom = { uploadInfo ->
                uploadInfo.uploadId.startsWith(
                    if (args.isUpdate)
                        Constants.COURSE_VIDEO_UPDATE_UPLOAD_MODULE_KEY
                    else
                        Constants.COURSE_VIDEO_UPLOAD_MODULE_KEY
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

                                viewLifecycleScope?.launch(Dispatchers.Main) {

                                    when (uploadType) {

                                        UploadType.COURSE_CODE_LINK -> {

                                            FileUtils.deleteRecursively(codeFile)
                                            if (fileInMb > 5) {
                                                toastL(getString(R.string.code_file_should_be_less_than_5mb))
                                                FileUtils.deleteRecursively(file)
                                                codeFile = null
                                                return@launch
                                            }

                                            binding.codeLinkName.text = file.name
                                            codeFile = file

                                        }

                                        UploadType.COURSE_HOME_WORK_LINK -> {

                                            FileUtils.deleteRecursively(homeWorkFile)
                                            if (fileInMb > 5) {
                                                toastL(getString(R.string.home_work_should_be_less_than_5mb))
                                                FileUtils.deleteRecursively(file)
                                                homeWorkFile = null
                                                return@launch
                                            }

                                            binding.homeWorkName.text = file.name
                                            homeWorkFile = file

                                        }

                                        UploadType.COURSE_PROJECT_LINK -> {

                                            FileUtils.deleteRecursively(projectFile)
                                            if (fileInMb > 3) {
                                                toastL(getString(R.string.project_file_should_be_less_than_3mb))
                                                FileUtils.deleteRecursively(file)
                                                projectFile = null
                                                return@launch
                                            }

                                            binding.projectName.text = file.name
                                            projectFile = file

                                        }

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

    fun codeLink() {
        uploadType = UploadType.COURSE_CODE_LINK
        context?.makeStoragePermission(mimeTypesOfEverything, startForResult)
    }

    fun homeWorkLink() {
        uploadType = UploadType.COURSE_HOME_WORK_LINK
        context?.makeStoragePermission(mimeTypesOfPdf, startForResult)
    }

    fun projectLink() {
        uploadType = UploadType.COURSE_PROJECT_LINK
        context?.makeStoragePermission(mimeTypesOfPdf, startForResult)
    }

    override fun onBackPressed(): Boolean {
        return false
    }

    fun submitData() {

        binding.apply {
            lectureName.error = null
            videoDetails.error = null

            when {

                lectureName.text.toString().length < 3 -> {
                    lectureName.error = getString(R.string.min_3_letter_required_in_course_type_field)
                    lectureName.requestFocus()
                }

                (videoData == null && args.isUpdate.not()) -> {
                    videoDetails.error = getString(R.string.choose_a_video)
                    videoDetails.requestFocus()
                }

                (codeFile == null || codeFile?.exists()?.not() == true) && args.isUpdate.not() -> {
                    toastL(getString(R.string.choose_a_code_file))
                }

                (homeWorkFile == null || homeWorkFile?.exists()?.not() == true) && args.isUpdate.not() -> {
                    toastL(getString(R.string.choose_a_home_work_file))
                }

                (projectFile == null || projectFile?.exists()?.not() == true) && args.isUpdate.not() -> {
                    toastL(getString(R.string.choose_a_project_file))
                }

                args.isUpdate -> {
                    viewModel.startLoading()

                    if (displayOrder.text.toString().isEmpty()) {
                        displayOrder.error = getString(R.string.display_order_should_not_be_empty)
                        displayOrder.requestFocus()
                        return@apply
                    }

                    updateCourseVideoUploadFile(
                        lectureName = lectureName.text.toString(),
                        videoId = videoData?.videoId.value,
                        codeFile = codeFile,
                        homeWorkFile = homeWorkFile,
                        courseId = args.courseId.value,
                        projectFile = projectFile,
                        fileUploaderService = fileUploaderService,
                        displayNumber = args.data?.displayOrder.value,
                        courseVideoId = args.data?.id.value
                    )
                }

                else -> {
                    viewModel.startLoading()
                    setCourseVideoUploadFile(
                        lectureName = lectureName.text.toString(),
                        codeFile = codeFile,
                        homeWorkFile = homeWorkFile,
                        projectFile = projectFile,
                        courseId = args.courseId,
                        fileUploaderService = fileUploaderService,
                        videoId = videoData?.videoId.value
                    )
                }
            }
        }

    }

    enum class UploadType {
        COURSE_CODE_LINK,
        COURSE_HOME_WORK_LINK,
        COURSE_PROJECT_LINK,
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
    }

    override fun onSuccess(context: Context, uploadInfo: UploadInfo, serverResponse: ServerResponse) {
        viewModel.stopLoading()

        if (args.isUpdate.not())
            binding.apply {
                videoData = null
                videoNameEt.setText(String.EMPTY)
                lectureName.setText(String.EMPTY)
                codeLinkName.text = getString(R.string.drag_amp_drop_code_file)
                homeWorkName.text = getString(R.string.drag_amp_drop_home_work_pdf)
                projectName.text = getString(R.string.drag_amp_drop_project_pdf)

                codeFile = null
                homeWorkFile = null
                projectFile = null

                executePendingBindings()
            }

        toastL(getString(R.string.upload_successfully))
    }

}