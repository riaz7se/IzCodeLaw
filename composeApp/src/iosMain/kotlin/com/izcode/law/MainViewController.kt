package com.izcode.law

import androidx.compose.ui.window.ComposeUIViewController
import com.izcode.law.document.handler.AttachmentHandler
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    // Create a mutable reference to hold the viewController
    var viewControllerRef: UIViewController? = null
    
    val viewController = ComposeUIViewController { 
        // Create iOS-specific AttachmentHandler using the captured reference
        val attachmentHandler = viewControllerRef?.let { AttachmentHandler(it) }
            ?: throw IllegalStateException("ViewControllerRef not initialized")
            
        App(attachmentHandler = attachmentHandler)
    }
    
    // Assign the created viewController to our reference
    viewControllerRef = viewController
    
    return viewController
}