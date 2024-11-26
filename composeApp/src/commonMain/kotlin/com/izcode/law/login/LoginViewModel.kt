package com.izcode.law.login

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel {
    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    // Hardcoded credentials - in real app these should be in a secure storage
    private val VALID_USERNAME = "admin"
    private val VALID_PASSWORD = "admin"

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnUsernameChange -> {
                _state.value = _state.value.copy(
                    username = event.username,
                    errorMessage = null
                )
            }
            is LoginEvent.OnPasswordChange -> {
                _state.value = _state.value.copy(
                    password = event.password,
                    errorMessage = null
                )
            }
            is LoginEvent.OnLoginClick -> validateAndLogin()
        }
    }

    private fun validateAndLogin() {
        val currentState = _state.value
        
        _state.value = currentState.copy(isLoading = true)

        if (currentState.username.isBlank() || currentState.password.isBlank()) {
            _state.value = currentState.copy(
                errorMessage = "Please fill in all fields",
                isLoading = false
            )
            return
        }

        if (currentState.username == VALID_USERNAME && 
            currentState.password == VALID_PASSWORD) {
            _state.value = currentState.copy(
                isLoginSuccessful = true,
                isLoading = false,
                errorMessage = null
            )
        } else {
            _state.value = currentState.copy(
                errorMessage = "Invalid username or password",
                isLoading = false
            )
        }
    }
} 