package ru.normno.myfirebasefirestoreandroid

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirebaseClient {
    private val tag: String = "FirebaseClient"
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val collection: String = "user"

    fun insertUser(): Flow<String?> {
        return callbackFlow {
            trySend(null)
            awaitClose()
        }
    }

    fun updateUser(): Flow<User?>{
        TODO()
    }

    fun getUser(){
        TODO()
    }
}