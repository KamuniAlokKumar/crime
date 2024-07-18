package uk.ac.tees.mad.w9642833

import android.net.Uri
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await
import uk.ac.tees.mad.w9642833.models.CrimeReport

class FirestoreRepository {
    private val firestore = Firebase.firestore
    private val storage = Firebase.storage

    suspend fun uploadImageToStorage(imageUri: Uri): String? {
        return try {
            val storageRef = storage.reference.child("crime_reports/${imageUri.lastPathSegment}")
            storageRef.putFile(imageUri).await()
            storageRef.downloadUrl.await().toString()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun submitCrimeReport(crimeReport: CrimeReport): Boolean {
        return try {
            firestore.collection("crime_reports").add(crimeReport).await()
            true
        } catch (e: Exception) {
            false
        }
    }
}