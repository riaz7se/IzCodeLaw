package com.izcode.law

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.izcode.law.auth.GoogleSignInManager
import com.izcode.law.document.handler.AttachmentHandler
import com.google.firebase.Firebase
import com.google.firebase.initialize
import co.touchlab.kermit.Logger
import com.izcode.law.auth.state.UserState

class MainActivity : ComponentActivity() {
    private lateinit var attachmentHandler: AttachmentHandler
    private lateinit var googleSignInManager: GoogleSignInManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize Kermit Logger
        initKermitLogger()
        
        // Initialize Firebase
        Firebase.initialize(this)
        Logger.i { "Firebase initialized" }
        
        // Initialize handlers
        attachmentHandler = AttachmentHandler(this)
        Logger.i { "AttachmentHandler initialized" }
        
        googleSignInManager = GoogleSignInManager(this)
        Logger.i { "Google Sign In Manager initialized" }
        
        setContent {
            App(
                attachmentHandler = attachmentHandler,
                googleSignInManager = googleSignInManager
            )
        }
    }

    private fun initKermitLogger() {
        Logger.i { "MainActivity: Initializing app..." }
    }
}