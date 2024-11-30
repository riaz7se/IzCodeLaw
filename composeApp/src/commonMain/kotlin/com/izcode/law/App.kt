package com.izcode.law

import HomeScreen
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import co.touchlab.kermit.Logger
import com.izcode.law.login.LoginScreen
import com.izcode.law.document.handler.AttachmentHandler
import com.izcode.law.auth.GoogleSignInManager
import kotlinx.coroutines.launch

@Composable
fun App(
    attachmentHandler: AttachmentHandler,
    googleSignInManager: GoogleSignInManager? = null
) {
    MaterialTheme {
        var isLoggedIn by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()

        if (!isLoggedIn) {
            LoginScreen(
                onLoginSuccess = {
                    Logger.i { "Login successful" }
                    isLoggedIn = true
                },
                googleSignInManager = googleSignInManager
            )
        } else {
            HomeScreen(
                attachmentHandler = attachmentHandler,
                onSignOut = {
                    scope.launch {
                        try {
                            googleSignInManager?.signOut()
                            isLoggedIn = false
                            Logger.i { "Sign out successful" }
                        } catch (e: Exception) {
                            Logger.e { "Sign out failed: ${e.message}" }
                        }
                    }
                }
            )
        }
    }
}