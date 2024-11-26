package com.izcode.law

import HomeScreen
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import com.izcode.law.login.LoginScreen
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.izcode.law.document.handler.AttachmentHandler

@Composable
@Preview
fun App(
    attachmentHandler: AttachmentHandler
) {
    MaterialTheme {

        var isLoggedIn by remember { mutableStateOf(false) }

        if (!isLoggedIn) {
            LoginScreen(
                onLoginSuccess = {
                    isLoggedIn = true
                    // Here you can add additional logic after successful login
                }
            )
        } else {
            HomeScreen(attachmentHandler = attachmentHandler)
        }

//        var showContent by remember { mutableStateOf(false) }
//        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//            Button(onClick = { showContent = !showContent }) {
//                Text("Click me!")
//            }
//            AnimatedVisibility(showContent) {
//                val greeting = remember { Greeting().greet() }
//                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//                    Image(painterResource(Res.drawable.compose_multiplatform), null)
//                    Text("Compose: $greeting")
//                }
//            }
//        }
    }
}