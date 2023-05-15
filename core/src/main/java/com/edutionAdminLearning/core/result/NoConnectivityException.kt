package com.edutionAdminLearning.core.result

import java.io.IOException

class NoConnectivityException : IOException() {
    override val message: String = "No internet connection"
}
