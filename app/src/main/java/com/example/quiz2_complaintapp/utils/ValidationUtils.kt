package com.example.quiz2_complaintapp.utils

/**
 * Utility object for validating complaint form fields.
 * Keeps validation logic out of Activities.
 */
object ValidationUtils {

    data class ValidationResult(
        val isValid: Boolean,
        val errorMessage: String = ""
    )

    fun validateStudentName(name: String): ValidationResult {
        return when {
            name.isBlank() -> ValidationResult(false, "Student name is required")
            name.trim().length < 3 -> ValidationResult(false, "Name must be at least 3 characters")
            !name.trim().matches(Regex("^[a-zA-Z ]+$")) ->
                ValidationResult(false, "Name should contain only letters")
            else -> ValidationResult(true)
        }
    }

    fun validateRollNumber(rollNumber: String): ValidationResult {
        return when {
            rollNumber.isBlank() -> ValidationResult(false, "Roll number is required")
            rollNumber.trim().length < 4 -> ValidationResult(false, "Invalid roll number format")
            else -> ValidationResult(true)
        }
    }

    fun validateComplaintTitle(title: String): ValidationResult {
        return when {
            title.isBlank() -> ValidationResult(false, "Complaint title is required")
            title.trim().length < 5 -> ValidationResult(false, "Title must be at least 5 characters")
            title.trim().length > 100 -> ValidationResult(false, "Title is too long (max 100 chars)")
            else -> ValidationResult(true)
        }
    }

    fun validateCategory(category: String): ValidationResult {
        return if (category == "Select Category" || category.isBlank()) {
            ValidationResult(false, "Please select a complaint category")
        } else {
            ValidationResult(true)
        }
    }

    fun validatePriority(priority: String): ValidationResult {
        return if (priority == "Select Priority" || priority.isBlank()) {
            ValidationResult(false, "Please select a priority level")
        } else {
            ValidationResult(true)
        }
    }

    fun validateDescription(description: String): ValidationResult {
        return when {
            description.isBlank() -> ValidationResult(false, "Complaint description is required")
            description.trim().length < 10 ->
                ValidationResult(false, "Description must be at least 10 characters")
            description.trim().length > 500 ->
                ValidationResult(false, "Description is too long (max 500 chars)")
            else -> ValidationResult(true)
        }
    }

    /**
     * Validates all fields at once.
     * Returns first error found, or success if all pass.
     */
    fun validateAll(
        name: String,
        rollNumber: String,
        title: String,
        category: String,
        priority: String,
        description: String
    ): ValidationResult {
        val checks = listOf(
            validateStudentName(name),
            validateRollNumber(rollNumber),
            validateComplaintTitle(title),
            validateCategory(category),
            validatePriority(priority),
            validateDescription(description)
        )
        return checks.firstOrNull { !it.isValid } ?: ValidationResult(true)
    }
}