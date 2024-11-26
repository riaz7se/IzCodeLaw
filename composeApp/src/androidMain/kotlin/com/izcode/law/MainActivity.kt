package com.izcode.law

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import co.touchlab.kermit.Logger
import com.izcode.law.document.handler.AttachmentHandler

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize logger before any operations
        initKermitLogger()

        // Create Android-specific AttachmentHandler on IO dispatcher
        val attachmentHandler = AttachmentHandler(this)

        setContent {
            App(attachmentHandler = attachmentHandler)
        }
    }

    private fun initKermitLogger() {
        // Initialize Kermit logger with Android configuration
        Logger.i("MainActivity Initializing app...")
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    // Note: Preview might not work with actual handler
    //App(attachmentHandler = AttachmentHandler(null/* context */ ))
}