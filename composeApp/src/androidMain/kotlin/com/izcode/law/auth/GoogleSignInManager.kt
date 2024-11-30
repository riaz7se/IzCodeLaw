package com.izcode.law.auth

import android.content.IntentSender
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.suspendCancellableCoroutine
import co.touchlab.kermit.Logger
import kotlin.coroutines.resume

actual class GoogleSignInManager(private val activity: ComponentActivity) {
    private val oneTapClient: SignInClient = Identity.getSignInClient(activity)
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var signInLauncher: ActivityResultLauncher<IntentSenderRequest>
    private var signInContinuation: ((Result<AuthResult>) -> Unit)? = null
    
    private val signInRequest = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId("418472165736-6qv98u653e5llth65gt1lebo7liuhce1.apps.googleusercontent.com") // Get this from Google Cloud Console
                .setFilterByAuthorizedAccounts(false)
                .build()
        )
        .build()

    init {
        signInLauncher = activity.registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            try {
                val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
                val idToken = credential.googleIdToken
                if (idToken != null) {
                    signInWithFirebase(idToken)
                } else {
                    signInContinuation?.invoke(Result.failure(Exception("No ID token!")))
                }
            } catch (e: Exception) {
                Logger.e { "Sign in failed: ${e.message}" }
                signInContinuation?.invoke(Result.failure(e))
            }
        }
    }

    actual suspend fun signIn(): Result<AuthResult> = suspendCancellableCoroutine { continuation ->
        signInContinuation = { result ->
            continuation.resume(result)
        }
        
        try {
            Logger.i { "Starting Google Sign In" }
            
            oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener { result ->
                    val intentSenderRequest = IntentSenderRequest.Builder(result.pendingIntent.intentSender)
                        .build()
                    signInLauncher.launch(intentSenderRequest)
                }
                .addOnFailureListener { e ->
                    Logger.e { "Sign in failed: ${e.message}" }
                    continuation.resume(Result.failure(e))
                }
                
        } catch (e: Exception) {
            Logger.e { "Sign in failed: ${e.message}" }
            continuation.resume(Result.failure(e))
        }
    }

    private fun signInWithFirebase(idToken: String) {
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(firebaseCredential)
            .addOnSuccessListener { authResult ->
                val result = AuthResult(
                    userId = authResult.user?.uid ?: "",
                    email = authResult.user?.email,
                    displayName = authResult.user?.displayName,
                    photoUrl = authResult.user?.photoUrl?.toString()
                )
                signInContinuation?.invoke(Result.success(result))
            }
            .addOnFailureListener { e ->
                signInContinuation?.invoke(Result.failure(e))
            }
    }

    actual suspend fun signOut() {
        try {
            auth.signOut()
            oneTapClient.signOut().await()
            Logger.i { "Sign out successful" }
        } catch (e: Exception) {
            Logger.e { "Sign out failed: ${e.message}" }
            throw e
        }
    }
} 