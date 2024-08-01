package uk.ac.tees.mad.w9642833

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await
import uk.ac.tees.mad.w9642833.models.CrimeReport
import uk.ac.tees.mad.w9642833.models.Criminal

class FirestoreRepository {
    private val firestore = Firebase.firestore
    private val storage = Firebase.storage

    suspend fun uploadImageToStorage(imageUri: ByteArray): String? {
        return try {
            val storageRef = storage.reference.child("crime_reports/${imageUri.hashCode()}")
            storageRef.
            putBytes(imageUri).await()
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

    suspend fun getAllCrimeReports(): List<CrimeReport> {
        val crimeReports = mutableListOf<CrimeReport>()
        try {
            val snapshot = firestore.collection("crime_reports").get().await()
            snapshot.documents.map { document ->
                println("${document.id} => ${document.data}")
                crimeReports.add(document.toObject(CrimeReport::class.java)!!)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return crimeReports
    }

    suspend fun getAllWantedCriminals(): List<Criminal> {
        val criminals = mutableListOf<Criminal>()

        try {
            val snapshot = firestore.collection("criminals").get().await()
            snapshot.documents.map { document ->
                println("${document.id} => ${document.data}")
                criminals.add(document.toObject(Criminal::class.java)!!)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return criminals
    }
}