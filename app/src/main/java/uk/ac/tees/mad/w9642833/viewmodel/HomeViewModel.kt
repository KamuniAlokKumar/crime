package uk.ac.tees.mad.w9642833.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.w9642833.FirestoreRepository
import uk.ac.tees.mad.w9642833.models.CrimeReport

class HomeViewModel : ViewModel() {

    private val repository = FirestoreRepository()
    private val _crimeReports = MutableStateFlow<List<CrimeReport>>(emptyList())
    val crimeReports: StateFlow<List<CrimeReport>> = _crimeReports

    init {
        viewModelScope.launch {
            _crimeReports.value = repository.getAllCrimeReports()
        }
    }
}
