package com.mysillydeams.app

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mysillydeams.app.auth.AuthState
import com.mysillydeams.app.auth.AuthViewModel
import com.mysillydeams.app.auth.AuthViewModelFactory
import com.mysillydeams.app.config.AppConfig
import com.mysillydeams.app.ui.screens.HomeScreen
import com.mysillydeams.app.ui.screens.LoginScreen
import com.mysillydeams.app.ui.theme.MySillyDreamsTheme

class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Validate configuration on startup
        try {
            AppConfig.validateConfiguration()
        } catch (e: IllegalStateException) {
            Toast.makeText(this, "Configuration Error: ${e.message}", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        setContent {
            MySillyDreamsTheme {
                val systemUiController = rememberSystemUiController()
                val authState by authViewModel.authState.collectAsStateWithLifecycle()
                val currentUser by authViewModel.currentUser.collectAsStateWithLifecycle()
                val uiState by authViewModel.uiState.collectAsStateWithLifecycle()

                // Make status bar transparent
                systemUiController.setSystemBarsColor(
                    color = androidx.compose.ui.graphics.Color.Transparent,
                    darkIcons = true
                )

                // Handle error messages
                LaunchedEffect(uiState.errorMessage) {
                    uiState.errorMessage?.let { message ->
                        Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
                        authViewModel.clearError()
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    when (authState) {
                        is AuthState.Loading -> {
                            // Show loading screen or keep current screen
                            if (currentUser == null) {
                                LoginScreen(
                                    onGoogleSignIn = { authViewModel.signInWithGoogle() },
                                    isLoading = uiState.isLoading
                                )
                            } else {
                                HomeScreen(
                                    user = currentUser,
                                    onSignOut = { authViewModel.signOut() }
                                )
                            }
                        }
                        is AuthState.Authenticated -> {
                            HomeScreen(
                                user = currentUser,
                                onSignOut = { authViewModel.signOut() }
                            )
                        }
                        is AuthState.Unauthenticated -> {
                            LoginScreen(
                                onGoogleSignIn = { authViewModel.signInWithGoogle() },
                                isLoading = uiState.isLoading
                            )
                        }
                        is AuthState.Error -> {
                            LoginScreen(
                                onGoogleSignIn = { authViewModel.signInWithGoogle() },
                                isLoading = uiState.isLoading
                            )
                        }
                    }
                }
            }
        }
    }
}
