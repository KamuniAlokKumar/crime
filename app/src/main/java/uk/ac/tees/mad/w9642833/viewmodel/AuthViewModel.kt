package uk.ac.tees.mad.w9642833.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel : ViewModel() {

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn = _isLoggedIn.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    val auth = Firebase.auth

    fun login(email: String, password: String, context: Context) {
        _isLoading.value = true
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            _isLoggedIn.value = true
            _isLoading.value = false
            Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener { e ->
            _isLoading.value = false
            e.printStackTrace()
            Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
        }
    }

    fun register(email: String, password: String, context: Context) {
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            _isLoggedIn.value = true
            _isLoading.value = false
            Toast.makeText(context, "Registration successful", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener { e ->
            e.printStackTrace()
            _isLoading.value = false
            Toast.makeText(context, "Registration failed", Toast.LENGTH_SHORT).show()
        }
    }
}