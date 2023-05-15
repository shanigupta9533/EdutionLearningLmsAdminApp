package com.edutionAdminLearning.core.type.validation

data class ValidationResult(
    val valid: Boolean,
    val errorMsg: String? = null
) {
    val notValid: Boolean
            get() = !valid
}
