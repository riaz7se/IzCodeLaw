package com.izcode.law.auth.state

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import co.touchlab.kermit.Logger
import com.izcode.law.auth.UserProfile

object UserState {
    private val _user = MutableStateFlow<UserProfile?>(null)
    val user: StateFlow<UserProfile?> = _user.asStateFlow()

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated.asStateFlow()

    fun updateUser(userProfile: UserProfile?) {
        Logger.d { "Updating user: ${userProfile?.displayName}" }
        _user.value = userProfile
        _isAuthenticated.value = userProfile != null
        Logger.d { "Auth state updated: ${_isAuthenticated.value}" }
    }

    fun clearUser() {
        Logger.d { "Clearing user state" }
        _user.value = null
        _isAuthenticated.value = false
    }
}