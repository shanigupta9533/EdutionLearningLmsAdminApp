package com.edutionAdminLearning.edutionlearningadminapp.data.remote

import javax.inject.Inject

class LmsEdutionDataSourceImpl @Inject constructor(
    private val lmsApi: LmsApi
) : LmsEdutionDataSource {
}