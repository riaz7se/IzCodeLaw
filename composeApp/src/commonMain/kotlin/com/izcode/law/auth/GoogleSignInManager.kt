package com.izcode.law.auth

expect class GoogleSignInManager {
    suspend fun signIn(): Result<UserProfile>
    suspend fun signOut()
}

// Common data class for auth result
data class AuthResult(
    val userId: String,
    val email: String?,
    val displayName: String?,
    val photoUrl: String?
)

// Common data class for auth result
data class UserProfile(
    val userId: String,
    val email: String?,
    val displayName: String?,
    val photoUrl: String?
)