import com.izcode.law.auth.UserProfile

object AuthManager {
    private var _currentUser: UserProfile? = null
    val currentUser: UserProfile?
        get() = _currentUser

    fun updateUser(authResult: UserProfile?) {
        _currentUser = authResult
    }

    fun isUserLoggedIn(): Boolean {
        return _currentUser != null
    }

    fun logout() {
        _currentUser = null
    }
} 