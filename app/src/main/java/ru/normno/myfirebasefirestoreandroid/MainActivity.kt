package ru.normno.myfirebasefirestoreandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.normno.myfirebasefirestoreandroid.ui.theme.MyFirebaseFirestoreAndroidTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val firebaseClient = FirebaseClient()

        setContent {
            MyFirebaseFirestoreAndroidTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    var localUser by remember {
                        mutableStateOf(
                            User(
                                id = "",
                                name = "Sergey",
                                email = "serega.b301@gmail.com",
                                age = 22,
                            )
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        TextField(
                            value = localUser.name,
                            onValueChange = { localUser = localUser.copy(name = it) },
                            label = { Text(text = "Name") }
                        )
                        TextField(
                            value = localUser.email,
                            onValueChange = { localUser = localUser.copy(email = it) },
                            label = { Text(text = "Email") }
                        )
                        TextField(
                            value = localUser.age.toString(),
                            onValueChange = {
                                if (it.isDigitsOnly()) {
                                    localUser = localUser.copy(age = it.toInt())
                                } else {
                                    Unit
                                }
                            },
                            label = { Text(text = "Age") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        )
                        Button(
                            onClick = {
                                lifecycleScope.launch {
                                    firebaseClient.insertUser(localUser).collect { id ->
                                        localUser = localUser.copy(id = id ?: "")
                                    }
                                }
                            }
                        ) {
                            Text(
                                text = "Insert"
                            )
                        }
                        Button(
                            onClick = {
                                localUser = localUser.copy(email = "serega.b302@yanex.ru")
                                lifecycleScope.launch {
                                    firebaseClient.updateUser(localUser).collect { result ->
                                        if (result) {
                                            println("user updated")
                                        } else {
                                            println("user not updated")
                                        }
                                    }
                                }
                            }
                        ) {
                            Text(
                                text = "Update"
                            )
                        }
                        Button(
                            onClick = {
                                lifecycleScope.launch {
                                    firebaseClient.getUser(localUser.email).collect { user ->
                                        if (user != null) {
                                            localUser = user
                                            println("user found: $user")
                                        } else {
                                            println("user not found")
                                        }
                                    }
                                }
                            }
                        ) {
                            Text(
                                text = "Get User"
                            )
                        }
                    }
                }
            }
        }
    }
}