package ru.normno.myfirebasefirestoreandroid

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class FirebaseClient {
    private val tag: String = "FirebaseClient"
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val collection: String = "user"

    fun insertUser(user: User): Flow<String?> {
        return callbackFlow {
            db.collection(collection)
                .add(user.toHashMap())
                .addOnSuccessListener { document ->
                    println(tag + "insert user with id: ${document.id}")
                    CoroutineScope(Dispatchers.IO).launch {
                        updateUser(user.copy(id = document.id)).collect {}
                    }
                    trySend(document.id)
                }
                .addOnFailureListener { error ->
                    error.printStackTrace()
                    println(tag + "insert user failed: ${error.message}")
                    trySend(null)
                }
            awaitClose {
                println(tag + "insert user close")
            }
        }
    }

    fun updateUser(user: User): Flow<Boolean> {
        return callbackFlow {
            db.collection(collection)
                .document(user.id)
                .set(user.toHashMap())
                .addOnSuccessListener { document ->
                    println(tag + "update user with id: ${user.id}")
                    trySend(true)
                }
                .addOnFailureListener { error ->
                    error.printStackTrace()
                    println(tag + "updating user failed: ${error.message}")
                    trySend(false)
                }
            awaitClose {
                println(tag + "insert user close")
            }
        }
    }

    fun getUser(
        email: String,
    ): Flow<User?> {
        return callbackFlow {
            db.collection(collection)
                .get()
                .addOnSuccessListener { result ->
                    var user: User? = null
                    result.forEach { document ->
                        if (document.get("email") == email) {
                            user = document.data.toUser()
                            println(tag + "user found with id: ${document.id}")
                            trySend(user)
                        }
                    }
                    if (user == null) {
                        println(tag + "user not found with email: $email")
                    }
                }
                .addOnFailureListener { error ->
                    error.printStackTrace()
                    println(tag + "getting user failed: ${error.message}")
                    trySend(null)
                }
            awaitClose {
                println(tag + "insert user close")
            }

        }
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
            age = (get("age") as Long).toInt(),
        )
    }
}