package com.izcode.law.auth

expect class GoogleSignInManager {
    suspend fun signIn(): Result<AuthResult>
    suspend fun signOut()
}

// Common data class for auth result
data class AuthResult(
    val userId: String,
    val email: String?,
    val displayName: String?,
    val photoUrl: String?
) 