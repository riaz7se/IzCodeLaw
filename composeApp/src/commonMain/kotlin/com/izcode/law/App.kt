package com.izcode.law

import HomeScreen
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import co.touchlab.kermit.Logger
import com.izcode.law.login.LoginScreen
import com.izcode.law.document.handler.AttachmentHandler
import com.izcode.law.auth.GoogleSignInManager
import com.izcode.law.auth.state.UserState
import kotlinx.coroutines.launch
import com.izcode.law.ui.theme.AppTheme
import com.izcode.law.profile.UserProfileScreen

@Composable
fun App(
    attachmentHandler: AttachmentHandler,
    googleSignInManager: GoogleSignInManager
) {
    val currentUser by UserState.user.collectAsState()
    val isAuthenticated by UserState.isAuthenticated.collectAsState()
    val scope = rememberCoroutineScope()
    var showProfile by remember { mutableStateOf(false) }

    AppTheme {
        if (!isAuthenticated) {
            LoginScreen(
                onLoginSuccess = {
                    Logger.d { "Login success, updating user state : ${currentUser}" }
                },
                googleSignInManager = googleSignInManager
            )
        } else {
            currentUser?.let { user ->
                if (showProfile) {
                    UserProfileScreen(
                        userProfile = user,
                        onBackPress = { showProfile = false }
                    )
                } else {
                    HomeScreen(
                        attachmentHandler = attachmentHandler,
                        userProfile = user,
                        onSignOut = {
                            scope.launch {
                                try {
                                    googleSignInManager.signOut()
                                    UserState.clearUser()
                                } catch (e: Exception) {
                                    Logger.e { "Sign out failed: ${e.message}" }
                                }
                            }
                        },
                        onProfileClick = { showProfile = true }
                    )
                }
            }
        }
    }
}