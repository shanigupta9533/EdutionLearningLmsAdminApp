package com.edutionAdminLearning.edutionLearningAdmin.fragment

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
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
import com.edutionAdminLearning.edutionLearningAdmin.databinding.FragmentBannerInsertBinding
import com.edutionAdminLearning.edutionLearningAdmin.utils.Constants.BANNER_UPLOAD_MODULE_KEY
import com.edutionAdminLearning.edutionLearningAdmin.utils.Constants.BANNER_UPLOAD_UPDATE_MODULE_KEY
import com.edutionAdminLearning.edutionLearningAdmin.utils.FilePickUtils
import com.edutionAdminLearning.edutionLearningAdmin.utils.IMAGE_EXTENSIONS
import com.edutionAdminLearning.edutionLearningAdmin.utils.makeStoragePermission
import com.edutionAdminLearning.edutionLearningAdmin.utils.setBannerUploadFile
import com.edutionAdminLearning.edutionLearningAdmin.utils.uniqueId
import com.edutionAdminLearning.edutionLearningAdmin.utils.updateBannerUploadFile
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
import javax.inject.Inject

@AndroidEntryPoint
class BannerInsertFragment : ViewModelBindingFragment<FragmentBannerInsertBinding, HomeDetailsViewModel>(
    FragmentBannerInsertBinding::inflate
), RequestObserverDelegate {

    @Inject
    lateinit var fileUploaderService: FileUploaderService
    private val args by navArgs<BannerInsertFragmentArgs>()
    override val viewModel: HomeDetailsViewModel by viewModels()
    private var bannerFile: File? = null

    override fun FragmentBannerInsertBinding.setViewBindingVariables() {
        toolbarText = getString(R.string.banner_details_insert)
        vm = viewModel
        controller = this@BannerInsertFragment
    }

    override fun FragmentBannerInsertBinding.setViewModelBindingData() {
        lifecycleOwner = viewLifecycleOwner

        if (args.isUpdate) {
            args.data?.let {
                bannerMessage.setText(it.messageText.value)
                submit.text = getString(R.string.update_banner_details)
                fileUrl = it.image
                bannerImageParent.visibility = View.VISIBLE
            }
        }

        toolbar.appCompatImageView.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        RequestObserver(requireContext(),
            requireActivity(),
            this@BannerInsertFragment,
            shouldAcceptEventsFrom = { uploadInfo ->
                uploadInfo.uploadId.startsWith(
                    if (args.isUpdate)
                        BANNER_UPLOAD_UPDATE_MODULE_KEY
                    else
                        BANNER_UPLOAD_MODULE_KEY
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
                                if (fileInMb > 1) {
                                    toastL(getString(R.string.image_size_should_be_less_than_1mb))
                                    return@getFile
                                }

                                binding.apply {
                                    viewLifecycleScope?.launch(Dispatchers.Main) {
                                        bannerImageParent.visibility = View.VISIBLE
                                        fileUrl = file.path
                                        executePendingBindings()
                                        bannerFile = file
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
        requireActivity().makeStoragePermission(IMAGE_EXTENSIONS, startForResult)
    }

    fun cancelImage() {
        binding.bannerImageParent.visibility = View.GONE
        bannerFile = null
    }

    fun uploadDocument() {

        binding.apply {

            bannerMessage.error = null
            when {

                bannerMessage.text.toString().length < 3 -> {
                    bannerMessage.error = getString(R.string.min_3_letter_required_in_course_type_field)
                    bannerMessage.requestFocus()
                }

                (bannerFile == null || bannerFile?.exists()?.not() == true) && args.isUpdate.not() -> {
                    toastL(getString(R.string.choose_a_image))
                }

                args.isUpdate -> {
                    viewModel.startLoading()
                    updateBannerUploadFile(
                        data = bannerFile,
                        messageText = binding.bannerMessage.text.toString(),
                        keywords = HOME_KEYWORDS,
                        fileUploaderService = fileUploaderService,
                        bannerId = args.data?.id.value
                    )
                }

                else -> {
                    viewModel.startLoading()
                    context?.apply {
                        setBannerUploadFile(
                            data = bannerFile,
                            messageText = binding.bannerMessage.text.toString(),
                            keywords = HOME_KEYWORDS,
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
    }

    override fun onSuccess(context: Context, uploadInfo: UploadInfo, serverResponse: ServerResponse) {
        viewModel.stopLoading()

        if (args.isUpdate.not())
            binding.apply {
                bannerFile = null
                bannerMessage.setText(String.EMPTY)
                bannerImageParent.visibility = View.GONE
                fileUrl = String.EMPTY
                executePendingBindings()
            }

        toastL(getString(R.string.upload_successfully))
    }
}