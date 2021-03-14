package se.ju.student.group16.androidproject

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

val firebaseRepository = FirebaseRepository().apply{}

class FirebaseRepository {
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    fun getAuth() = auth
    fun getCurrentUser() = auth.currentUser
    fun getDatabaseReference() = database
}