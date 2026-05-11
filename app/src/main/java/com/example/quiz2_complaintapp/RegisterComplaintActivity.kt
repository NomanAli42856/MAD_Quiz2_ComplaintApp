package com.example.quiz2_complaintapp

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.quiz2_complaintapp.databinding.ActivityRegisterComplaintBinding
import com.example.quiz2_complaintapp.utils.Constants
import com.google.android.material.snackbar.Snackbar

/**
 * Form screen for submitting a new complaint.
 * Reads user input → passes to ViewModel → ViewModel validates & saves to Firestore.
 */
class RegisterComplaintActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterComplaintBinding
    private val viewModel: RegisterComplaintViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterComplaintBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupSpinners()
        setupSubmitButton()
        observeViewModel()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Register Complaint"
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Back arrow
    }

    private fun setupSpinners() {

        val categoryAdapter = ArrayAdapter(
            this,
            R.layout.spinner_item,        // your custom layout — black text
            Constants.COMPLAINT_CATEGORIES
        )
        categoryAdapter.setDropDownViewResource(R.layout.spinner_item)
        binding.spinnerCategory.adapter = categoryAdapter

// Priority spinner
        val priorityAdapter = ArrayAdapter(
            this,
            R.layout.spinner_item,
            Constants.PRIORITY_LEVELS
        )
        priorityAdapter.setDropDownViewResource(R.layout.spinner_item)
        binding.spinnerPriority.adapter = priorityAdapter
    }

    private fun setupSubmitButton() {
        binding.btnSubmit.setOnClickListener {
            // Collect all field values from UI
            val studentName = binding.etStudentName.text.toString()
            val rollNumber = binding.etRollNumber.text.toString()
            val title = binding.etComplaintTitle.text.toString()
            val category = binding.spinnerCategory.selectedItem.toString()
            val priority = binding.spinnerPriority.selectedItem.toString()
            val description = binding.etDescription.text.toString()

            // Hand off to ViewModel (no logic here in the Activity)
            viewModel.submitComplaint(studentName, rollNumber, title, category, priority, description)
        }
    }

    private fun observeViewModel() {
        viewModel.submitState.observe(this) { state ->
            when (state) {
                is RegisterComplaintViewModel.SubmitState.Idle -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnSubmit.isEnabled = true
                }

                is RegisterComplaintViewModel.SubmitState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnSubmit.isEnabled = false
                }

                is RegisterComplaintViewModel.SubmitState.ValidationError -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnSubmit.isEnabled = true
                    Snackbar.make(binding.root, state.message, Snackbar.LENGTH_LONG).show()
                }

                is RegisterComplaintViewModel.SubmitState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnSubmit.isEnabled = true
                    Snackbar.make(binding.root, state.message, Snackbar.LENGTH_SHORT).show()
                    clearForm()
                    viewModel.resetState()
                }

                is RegisterComplaintViewModel.SubmitState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnSubmit.isEnabled = true
                    Snackbar.make(binding.root, state.message, Snackbar.LENGTH_LONG).show()
                    viewModel.resetState()
                }
            }
        }
    }

    /** Resets all form fields to default after successful submission */
    private fun clearForm() {
        binding.etStudentName.text?.clear()
        binding.etRollNumber.text?.clear()
        binding.etComplaintTitle.text?.clear()
        binding.etDescription.text?.clear()
        binding.spinnerCategory.setSelection(0)
        binding.spinnerPriority.setSelection(0)
    }

    // Handle toolbar back button
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}