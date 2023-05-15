package com.edutionLearning.edutionlearningapp.data.remote

import javax.inject.Inject

class LmsEdutionDataSourceImpl @Inject constructor(
    private val lmsApi: LmsApi
) : LmsEdutionDataSource {
}