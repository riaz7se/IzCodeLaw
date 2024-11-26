package com.izcode.law.login

data class LoginState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoginSuccessful: Boolean = false
)

sealed interface LoginEvent {
    data class OnUsernameChange(val username: String) : LoginEvent
    data class OnPasswordChange(val password: String) : LoginEvent
    object OnLoginClick : LoginEvent
} 