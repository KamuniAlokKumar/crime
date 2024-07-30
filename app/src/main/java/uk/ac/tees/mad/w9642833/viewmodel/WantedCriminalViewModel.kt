package uk.ac.tees.mad.w9642833.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.w9642833.FirestoreRepository
import uk.ac.tees.mad.w9642833.models.Criminal

class WantedCriminalViewModel: ViewModel() {

    private val repository = FirestoreRepository()
    private val _criminals = MutableStateFlow<List<Criminal>>(emptyList())
    val criminals: StateFlow<List<Criminal>> = _criminals

    init {
        viewModelScope.launch {
            _criminals.value = repository.getAllWantedCriminals()
        }
    }
}