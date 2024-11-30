package com.izcode.law.auth

import co.touchlab.kermit.Logger
import platform.UIKit.UIViewController

actual class GoogleSignInManager(private val viewController: UIViewController) {
    actual suspend fun signIn(): Result<AuthResult> {
        // iOS implementation will go here
        // For now, return failure
        Logger.i { "iOS Google Sign In not implemented yet" }
        return Result.failure(Exception("Not implemented for iOS yet"))
    }

    actual suspend fun signOut() {
        Logger.i { "iOS Google Sign Out not implemented yet" }
    }
} 