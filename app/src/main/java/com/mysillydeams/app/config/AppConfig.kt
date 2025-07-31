package com.mysillydeams.app.config

import com.mysillydeams.app.BuildConfig

/**
 * Application configuration validator and accessor
 * Ensures all required configuration values are present and valid
 */
object AppConfig {
    
    /**
     * Validates that all required configuration values are present
     * @throws IllegalStateException if any required configuration is missing
     */
    fun validateConfiguration() {
        val missingConfigs = mutableListOf<String>()
        
        if (BuildConfig.GOOGLE_WEB_CLIENT_ID.isEmpty()) {
            missingConfigs.add("GOOGLE_WEB_CLIENT_ID")
        }
        
        if (BuildConfig.FIREBASE_PROJECT_ID.isEmpty()) {
            missingConfigs.add("FIREBASE_PROJECT_ID")
        }
        
        if (BuildConfig.FIREBASE_API_KEY.isEmpty()) {
            missingConfigs.add("FIREBASE_API_KEY")
        }
        
        if (missingConfigs.isNotEmpty()) {
            throw IllegalStateException(
                "Missing required configuration values: ${missingConfigs.joinToString(", ")}. " +
                "Please check your .env file and ensure all required values are set."
            )
        }
    }
    
    /**
     * Gets the Google Web Client ID for OAuth authentication
     */
    val googleWebClientId: String
        get() = BuildConfig.GOOGLE_WEB_CLIENT_ID
    
    /**
     * Gets the Firebase Project ID
     */
    val firebaseProjectId: String
        get() = BuildConfig.FIREBASE_PROJECT_ID
    
    /**
     * Gets the Firebase API Key
     */
    val firebaseApiKey: String
        get() = BuildConfig.FIREBASE_API_KEY
    
    /**
     * Checks if the app is in debug mode
     */
    val isDebug: Boolean
        get() = BuildConfig.DEBUG
    
    /**
     * Gets the app version name
     */
    val versionName: String
        get() = BuildConfig.VERSION_NAME
    
    /**
     * Gets the app version code
     */
    val versionCode: Int
        get() = BuildConfig.VERSION_CODE
}
