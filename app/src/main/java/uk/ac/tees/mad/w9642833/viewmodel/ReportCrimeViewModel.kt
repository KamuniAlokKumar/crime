package uk.ac.tees.mad.w9642833.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uk.ac.tees.mad.w9642833.FirestoreRepository
import uk.ac.tees.mad.w9642833.models.CrimeReport

import android.net.Uri
import androidx.lifecycle.ViewModel

class ReportCrimeViewModel : ViewModel() {
    private val repository = FirestoreRepository()
    var submissionStatus by mutableStateOf<Boolean?>(null)
        private set

    fun submitCrimeReport(
        crimeTitle: String,
        crimeDetails: String,
        location: String,
        imageUri: Uri?
    ) {
        viewModelScope.launch {
            val imageUrl = imageUri?.let { repository.uploadImageToStorage(it) } ?: ""
            val crimeReport = CrimeReport(crimeTitle, crimeDetails, location, imageUrl)
            submissionStatus = repository.submitCrimeReport(crimeReport)
        }
    }
}