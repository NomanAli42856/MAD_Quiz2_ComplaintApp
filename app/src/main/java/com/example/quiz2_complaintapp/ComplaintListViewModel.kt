package com.example.quiz2_complaintapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quiz2_complaintapp.models.Complaint
import com.example.quiz2_complaintapp.utils.ComplaintRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * ViewModel for MainActivity.
 * Survives configuration changes (screen rotation).
 * Exposes complaints as LiveData so the UI can observe changes.
 */
class ComplaintListViewModel : ViewModel() {

    private val repository = ComplaintRepository()

    // Backing property (private, mutable)
    private val _complaints = MutableLiveData<List<Complaint>>()
    val complaints: LiveData<List<Complaint>> = _complaints

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    init {
        loadComplaints()
    }

    private fun loadComplaints() {
        _isLoading.value = true
        repository.getComplaintsFlow()
            .onEach { result ->
                _isLoading.value = false
                result.fold(
                    onSuccess = { list ->
                        _complaints.value = list
                        _error.value = null
                    },
                    onFailure = { e ->
                        _error.value = e.message ?: "Failed to load complaints"
                    }
                )
            }
            .launchIn(viewModelScope)
    }
}