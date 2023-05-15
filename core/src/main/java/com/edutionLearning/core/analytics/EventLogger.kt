package com.edutionLearning.core.analytics

import android.os.Bundle
import com.edutionLearning.core.utils.ConstantKeys
import com.google.firebase.analytics.FirebaseAnalytics

class EventLogger(
    private val firebaseAnalytics: FirebaseAnalytics,
    private val userId: String,
    private val mobileNumber: String,
    private val name: String
) {

    fun loginUser(
        userId: String,
        mobileNumber: String,
        name: String,
    ) = mutableMapOf<String, Any>(
        MOBILE_NUMBER to mobileNumber,
        USER_ID to userId,
        USER_NAME to name,
    ).logFirebase(USER_LOGIN_PAGE)

    fun enquiryNowAboutCourse(
    ) = mutableMapOf<String, Any>(
    ).logFirebase(ENQUIRY_FROM_PURCHASE_PAGE)

    fun signUpUser(
        userId: String,
        mobileNumber: String,
        name: String,
    ) = mutableMapOf<String, Any>(
        MOBILE_NUMBER to mobileNumber,
        USER_ID to userId,
        USER_NAME to name,
    ).logFirebase(SIGNUP_USER)

    fun otpVerifyUser(
    ) = mutableMapOf<String, Any>(
    ).logFirebase(USER_OTP_VERIFY)

    fun userVisitToCourse(
        courseId: String,
        courseName: String,
        isPurchase: Boolean,
        coursePurchaseDetails: String
    ) = mutableMapOf<String, Any>(
        COURSE_ID to courseId,
        COURSE_NAME to courseName,
        IS_PURCHASE to isPurchase,
        COURSE_PURCHASE_DETAILS to coursePurchaseDetails,
    ).logFirebase(COURSE_DETAILS_USER_CLICK)

    fun buyCoursesFromVideo(
    ) = mutableMapOf<String, Any>(
    ).logFirebase(COURSES_BUY_FROM_VIDEO)

    fun userClickToStartPurchase(
        courseId: String,
        courseName: String,
        coursePrice: String,
        coursePurchaseDetails: String
    ) = mutableMapOf<String, Any>(
        COURSE_ID to courseId,
        COURSE_NAME to courseName,
        COURSE_PRICE to coursePrice,
        COURSE_PURCHASE_DETAILS to coursePurchaseDetails,
    ).logFirebase(CLICK_TO_PURCHASE_BUTTON)

    fun homePage() =
        mutableMapOf<String, Any>(
        ).logFirebase(USER_VISIT_HOME_PAGE)

    @Synchronized
    fun MutableMap<String, Any>.logFirebase(eventType: String) {
        firebaseAnalytics.logEvent(eventType, intoBundle())
    }

    private fun MutableMap<String, Any>.intoBundle(): Bundle {
        val it: Iterator<Map.Entry<String, Any>> = entries.iterator()
        val bundle = Bundle()
        while (it.hasNext()) {
            val (key, value) = it.next()
            bundle.putString(key, value.toString())
        }
        addBaseParams(bundle)
        return bundle
    }

    private fun addBaseParams(bundle: Bundle) {
        bundle.putString(ConstantKeys.USER_ID, userId)
        bundle.putString(ConstantKeys.MOBILE_NUMBER, mobileNumber)
        bundle.putString(ConstantKeys.USER_NAME, name)
    }

    companion object {
        private const val USER_ID = "userId"
        private const val MOBILE_NUMBER = "mobileNumber"
        private const val USER_NAME = "userName"
        private const val COURSE_ID = "courseId"
        private const val COURSE_NAME = "courseName"
        private const val IS_PURCHASE = "isPurchase"
        private const val COURSE_PRICE = "course_price"
        private const val COURSE_PURCHASE_DETAILS = "course_purchase_details"
        private const val COURSES_BUY_FROM_VIDEO = "courses_buy_from_video"
        private const val USER_LOGIN_PAGE = "user_login_success"
        private const val ENQUIRY_FROM_PURCHASE_PAGE = "enquiry_from_purchase_page"
        private const val SIGNUP_USER = "user_signup_success"
        private const val USER_OTP_VERIFY = "user_otp_success"
        private const val USER_VISIT_HOME_PAGE = "visit_home_page"
        private const val COURSE_DETAILS_USER_CLICK = "course_details_user_click"
        private const val CLICK_TO_PURCHASE_BUTTON = "purchase_button_in_choose_plan_page"
    }

}