package com.example.quiz2_complaintapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quiz2_complaintapp.models.Complaint
import com.example.quiz2_complaintapp.utils.ComplaintRepository
import com.example.quiz2_complaintapp.utils.Constants
import com.example.quiz2_complaintapp.utils.ValidationUtils
import kotlinx.coroutines.launch

/**
 * ViewModel for RegisterComplaintActivity.
 * Handles form submission business logic.
 */
class RegisterComplaintViewModel : ViewModel() {

    private val repository = ComplaintRepository()

    // Sealed class represents all possible UI states
    sealed class SubmitState {
        object Idle : SubmitState()
        object Loading : SubmitState()
        data class Success(val message: String) : SubmitState()
        data class Error(val message: String) : SubmitState()
        data class ValidationError(val message: String) : SubmitState()
    }

    private val _submitState = MutableLiveData<SubmitState>(SubmitState.Idle)
    val submitState: LiveData<SubmitState> = _submitState

    /**
     * Validates all inputs then submits the complaint to Firestore.
     */
    fun submitComplaint(
        studentName: String,
        rollNumber: String,
        title: String,
        category: String,
        priority: String,
        description: String
    ) {
        // --- Validation ---
        val validation = ValidationUtils.validateAll(
            studentName, rollNumber, title, category, priority, description
        )
        if (!validation.isValid) {
            _submitState.value = SubmitState.ValidationError(validation.errorMessage)
            return
        }

        // --- Build model ---
        val complaint = Complaint(
            studentName = studentName.trim(),
            rollNumber = rollNumber.trim().uppercase(),
            complaintTitle = title.trim(),
            category = category,
            priority = priority,
            description = description.trim(),
            status = Constants.STATUS_PENDING
            // createdAt is set by Firestore @ServerTimestamp automatically
        )

        // --- Submit to Firestore ---
        _submitState.value = SubmitState.Loading
        viewModelScope.launch {
            val result = repository.submitComplaint(complaint)
            result.fold(
                onSuccess = {
                    _submitState.value = SubmitState.Success("Complaint submitted successfully!")
                },
                onFailure = { e ->
                    _submitState.value = SubmitState.Error(
                        e.message ?: "Failed to submit complaint. Check your internet connection."
                    )
                }
            )
        }
    }

    /** Reset state after the UI has handled the result */
    fun resetState() {
        _submitState.value = SubmitState.Idle
    }
}