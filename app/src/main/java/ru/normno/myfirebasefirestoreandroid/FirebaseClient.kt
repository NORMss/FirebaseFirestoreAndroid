package ru.normno.myfirebasefirestoreandroid

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirebaseClient {
    private val tag: String = "FirebaseClient"
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val collection: String = "user"

    fun insertUser(user: User): Flow<String?> {
        return callbackFlow {
            db.collection(collection)
                .add(user.toHashMap())
        }
    }

    fun updateUser(): Flow<User?> {
        TODO()
    }

    fun getUser() {
        TODO()
    }

    private fun User.toHashMap(): HashMap<String, Any> {
        return hashMapOf(
            "id" to id,
            "name" to name,
            "email" to email,
            "age" to age,
        )
    }

    private fun Map<String, Any>.toUser(): User {
        return User(
            id = get("id") as String,
            name = get("name") as String,
            email = get("email") as String,
            age = get("age") as Int,
        )
    }
}