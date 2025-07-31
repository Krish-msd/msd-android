package com.mysillydeams.app.auth

import android.content.Context
import android.os.CancellationSignal
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CredentialManagerCallback
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.ClearCredentialException
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.mysillydeams.app.config.AppConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import java.util.concurrent.Executors

class AuthRepository(private val context: Context) {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val credentialManager: CredentialManager = CredentialManager.create(context)

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _currentUser = MutableStateFlow<FirebaseUser?>(null)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser.asStateFlow()
    
    init {
        // Check if user is already signed in
        _currentUser.value = auth.currentUser
        _authState.value = if (auth.currentUser != null) {
            AuthState.Authenticated
        } else {
            AuthState.Unauthenticated
        }
        
        // Listen for auth state changes
        auth.addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            _currentUser.value = user
            _authState.value = if (user != null) {
                AuthState.Authenticated
            } else {
                AuthState.Unauthenticated
            }
        }
    }
    
    suspend fun signInWithGoogle(): Result<Unit> {
        return try {
            // Validate configuration on first access
            AppConfig.validateConfiguration()

            _authState.value = AuthState.Loading

            // Create Google ID option
            val googleIdOption = GetGoogleIdOption.Builder()
                .setServerClientId(AppConfig.googleWebClientId)
                .setFilterByAuthorizedAccounts(false)
                .build()

            // Create credential request
            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            // Get credential using Credential Manager
            val result = credentialManager.getCredential(
                request = request,
                context = context
            )

            handleSignInResult(result.credential)
            Result.success(Unit)
        } catch (e: GetCredentialException) {
            _authState.value = AuthState.Error(e.message ?: "Sign in failed")
            Result.failure(e)
        } catch (e: Exception) {
            _authState.value = AuthState.Error(e.message ?: "Unknown error occurred")
            Result.failure(e)
        }
    }
    
    private suspend fun handleSignInResult(credential: Credential): Result<FirebaseUser> {
        return try {
            // Check if credential is of type Google ID
            if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                // Create Google ID Token
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val idToken = googleIdTokenCredential.idToken

                // Sign in to Firebase using the token
                val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                val authResult = auth.signInWithCredential(firebaseCredential).await()
                val user = authResult.user

                if (user != null) {
                    _authState.value = AuthState.Authenticated
                    Result.success(user)
                } else {
                    _authState.value = AuthState.Error("Authentication failed")
                    Result.failure(Exception("Authentication failed"))
                }
            } else {
                _authState.value = AuthState.Error("Invalid credential type")
                Result.failure(Exception("Credential is not of type Google ID"))
            }
        } catch (e: GoogleIdTokenParsingException) {
            _authState.value = AuthState.Error("Failed to parse Google ID token")
            Result.failure(e)
        } catch (e: Exception) {
            _authState.value = AuthState.Error(e.message ?: "Authentication failed")
            Result.failure(e)
        }
    }
    
    fun signOut() {
        // Firebase sign out
        auth.signOut()

        // Clear credential state from all credential providers
        val clearRequest = ClearCredentialStateRequest()
        credentialManager.clearCredentialStateAsync(
            clearRequest,
            CancellationSignal(),
            Executors.newSingleThreadExecutor(),
            object : CredentialManagerCallback<Void?, ClearCredentialException> {
                override fun onResult(result: Void?) {
                    _authState.value = AuthState.Unauthenticated
                }

                override fun onError(e: ClearCredentialException) {
                    // Even if clearing fails, we still signed out from Firebase
                    _authState.value = AuthState.Unauthenticated
                }
            }
        )
    }
    
    fun getCurrentUser(): FirebaseUser? = auth.currentUser
}

sealed class AuthState {
    object Loading : AuthState()
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    data class Error(val message: String) : AuthState()
}
