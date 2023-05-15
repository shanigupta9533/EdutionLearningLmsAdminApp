package com.edutionLearning.core_ui.binding

import android.app.Activity
import android.content.Context
import android.content.res.TypedArray
import android.graphics.PorterDuff
import android.graphics.drawable.Animatable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Environment
import android.text.TextUtils
import android.util.TypedValue
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.edutionLearning.core_ui.R
import com.google.android.material.button.MaterialButton
import java.io.File
import java.util.concurrent.TimeUnit

const val MESSAGE_DENIED = "Permission Denied"
const val MAIN_FOLDER_NAME = "/physicsWallahUpload"
const val MAIN_FILE_NAME = "/PhysicsWallah"
const val RECORDING_FILE_LOCATION = "/recording"
const val CREATED_AUDIO_FILE_EXTENSION = ".mp3"
const val DOWNLOADING_FILE_LOCATION = "/download_files"

@BindingAdapter(value = ["imgUrl", "placeholder"], requireAll = false)
fun ImageView.setImgUrl(url: String?, placeholder: Drawable? = null) {
    asyncSrc(url, placeholder)
}

@BindingAdapter(value = ["imgUrl", "placeholder"], requireAll = false)
fun ImageView.setImgUrl(@RawRes @DrawableRes url: Int?, placeholder: Drawable? = null) {
    asyncSrc(url, placeholder)
}

@BindingAdapter(value = ["imgUrlCC", "placeholderCC", "compressed"], requireAll = false)
fun ImageView.setImgUrlCC(url: String?, placeholder: Drawable? = null, compressed: Int = 0) {
    asyncSrcCenterCrop(url, placeholder, compressed)
}

@BindingAdapter(
    value = ["imgUrlN", "placeholderN", "isCrop"], requireAll = false
)
fun ImageView.setImgUrlCircleN(url: String?, placeholder: Drawable? = null, isCrop: Boolean = false) {
    asyncSrcCrop(url, placeholder, isCrop)
}

@BindingAdapter(
    value = ["imgUrlCircle", "placeholderCircle", "compressedCircle"], requireAll = false
)
fun ImageView.setImgUrlCircle(url: String?, placeholder: Drawable? = null, compressed: Int = 0) {
    asyncSrcCenterCrop(url, placeholder, compressed, true)
}

@BindingAdapter(
    value = ["showProgressInButton", "progressColor", "setIcons", "isFileAvailable"], requireAll = false
)
fun MaterialButton.setButtonLoading(
    loading: Boolean, color: Int, iconsDrawable: Drawable?, isFileAvailable: Boolean
) {
    maxLines = 1
    ellipsize = TextUtils.TruncateAt.END
    iconGravity = MaterialButton.ICON_GRAVITY_START

    if (loading) {
        var drawable = icon
        if (drawable !is Animatable) {
            drawable = getProgressBarDrawable(context, color)
            icon = drawable
        }
        (drawable as Animatable).start()
    } else {
        icon = iconsDrawable
    }

    if (isFileAvailable) {
        icon = null
        text = "View"
    }

    iconGravity = MaterialButton.ICON_GRAVITY_TEXT_START

}

private fun getProgressBarDrawable(context: Context, color: Int): Drawable? {
    val value = TypedValue()
    context.theme.resolveAttribute(android.R.attr.progressBarStyleSmall, value, false)
    val progressBarStyle = value.data
    val attributes = intArrayOf(android.R.attr.indeterminateDrawable)
    val typedArray: TypedArray = context.obtainStyledAttributes(progressBarStyle, attributes)
    val drawable = typedArray.getDrawable(0)?.mutate()
    drawable?.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
    typedArray.recycle()

    return drawable
}

@BindingAdapter(
    value = ["imgUrlFileCC", "placeholderFileCC", "compressed", "imageName", "imageType"], requireAll = false
)
fun ImageView.setImgUrlFileCC(
    url: String?, placeholder: Drawable? = null, compressed: Int = 0, imageName: String? = null, type: String? = null
) {
    asyncSrcFileCenterCrop(url, placeholder, compressed, imageName, type)
}

@BindingAdapter(value = ["profileImgCC"], requireAll = false)
fun ImageView.setProfileCC(url: String?) {
    asyncProfileCenterCrop(url)
}

fun asyncProfileCenterCrop(url: String?) {

}

@BindingAdapter(value = ["timeCalculate"], requireAll = false)
fun TextView.audioTimeCalculate(
    duration: Long,
) {
    asynTimeChanging(duration)
}

fun TextView.asynTimeChanging(duration: Long) {

    var timestamp = duration
    val minute = TimeUnit.MILLISECONDS.toMinutes(timestamp)
    timestamp -= TimeUnit.MINUTES.toMillis(minute)
    val second = TimeUnit.MILLISECONDS.toSeconds(timestamp)

    val min = String.format("%02d", minute)
    val sec = String.format("%02d", second)

    this.text = "$min : $sec"

}


@BindingAdapter(value = ["imgUrlCCOrGray"], requireAll = false)
fun ImageView.setImgUrlCCOrGray(url: String?) {
    asyncSrcCenterCrop(
        url, ColorDrawable(ContextCompat.getColor(context, R.color.gray_E2E2E2)), 0
    )
}

@BindingAdapter(value = ["imgUrlOrWhite"], requireAll = false)
fun ImageView.setImgUrlOrWhite(url: String?) {
    asyncSrc(url, ColorDrawable(ContextCompat.getColor(context, R.color.white)))
}

@BindingAdapter(value = ["imgUrlOrGray"], requireAll = false)
fun ImageView.setImgUrlOrGray(url: String?) {
    asyncSrc(url, ColorDrawable(ContextCompat.getColor(context, R.color.gray_E2E2E2)))
}

fun ImageView.asyncSrc(url: String?, placeholder: Drawable?) {
    if (!url.isNullOrEmpty() && this.context.isAvailable()) {
        val requestOptions = RequestOptions().fitCenter()
        placeholder?.let {
            requestOptions.placeholder(it).error(it)
        }
        val requestCreator = Glide.with(this.context).load(url).apply(requestOptions).dontAnimate()
        requestCreator.into(this)
    }
}

fun ImageView.asyncSrc(@RawRes @DrawableRes url: Int?, placeholder: Drawable?) {
    if (url != null && this.context.isAvailable()) {
        val requestOptions = RequestOptions().fitCenter()
        placeholder?.let {
            requestOptions.placeholder(it).error(it)
        }
        val requestCreator = Glide.with(this.context).load(url).apply(requestOptions).dontAnimate()
        requestCreator.into(this)
    }
}

fun Context?.isAvailable(): Boolean {
    if (this == null) {
        return false
    }
    if (this is Activity) {
        if (this.isDestroyed || this.isFinishing) {
            return false
        }
    }
    return true
}

fun ImageView.asyncSrcCrop(url: String?, placeholder: Drawable?, isCrop: Boolean = false) {
    if (this.context.isAvailable()) {
        val requestOptions = if (isCrop) RequestOptions().centerCrop() else RequestOptions()
        placeholder?.let { requestOptions.placeholder(it).error(it) }
        val requestCreator = Glide.with(this.context).load(url).apply(requestOptions).dontAnimate()
        requestCreator.into(this)
    }
}

fun ImageView.asyncSrcCenterCrop(
    url: String?, placeholder: Drawable?, compressed: Int, circle: Boolean = false
) {

    if (this.context.isAvailable()) {
        var requestOptions = if (circle) RequestOptions().circleCrop()
        else RequestOptions().centerCrop()
        if (compressed > 0) requestOptions = requestOptions.override(compressed)

        placeholder?.let {
            requestOptions.placeholder(it).error(it)
        }

        val requestCreator = Glide.with(this.context).let {
            if (!url.isNullOrEmpty()) it.load(url)
            else if (placeholder != null) it.load(placeholder)
            else null
        }?.transition(withCrossFade())?.apply(requestOptions)
        requestCreator?.into(this)
    }
}

fun ImageView.asyncSrcFileCenterCrop(url: String?, placeholder: Drawable?, compressed: Int, imageName: String?, type: String?) {

    if (type.isNullOrEmpty().not() && this.context.isAvailable() && type.equals("image", true)) {

        var file: File? = null

        imageName?.let {
            file = context.checkFileLocation(it)
        }

        var requestOptions = RequestOptions().centerCrop()

        if (compressed > 0 && (file?.exists() == false || file?.length()?.compareTo(0L) == 0)) requestOptions = requestOptions.override(compressed)

        placeholder?.let {
            requestOptions.placeholder(it).error(it)
        }

        val requestCreator = Glide.with(this.context).let {
            if (file != null && file?.exists() == true && (file?.length() ?: 0L) > 0) {
                it.load(file).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
            } else if (!url.isNullOrEmpty()) it.load(url).diskCacheStrategy(DiskCacheStrategy.ALL)
            else if (placeholder != null) it.load(placeholder)
            else null
        }?.apply(requestOptions)?.dontAnimate()
        requestCreator?.into(this)

    }
}

fun Context.checkFileLocation(fileName: String): File? {

    if (TextUtils.isEmpty(fileName)) return null

    val file: File = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        File(
            this.getExternalFilesDir(null).toString() + "${MAIN_FOLDER_NAME}${DOWNLOADING_FILE_LOCATION}/${fileName}"
        )
    } else {
        File(Environment.getExternalStorageDirectory().absolutePath + "${MAIN_FOLDER_NAME}${DOWNLOADING_FILE_LOCATION}/${fileName}")
    }

    return file

}

@BindingAdapter(value = ["imgResId"], requireAll = false)
fun ImageView.setImgResId(@DrawableRes resId: Int) {
    setImageDrawable(ContextCompat.getDrawable(context, resId))
}