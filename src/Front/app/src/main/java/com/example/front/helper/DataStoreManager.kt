package com.example.front.helper

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.nimbusds.jose.JWSObject
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreManager @Inject constructor(@ApplicationContext context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token_datastore")
    private val dataStore: DataStore<Preferences> = context.dataStore

    // Define a key for the token
    private val TOKEN_KEY = stringPreferencesKey("token")

    // Function to store the token
    suspend fun storeToken(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    // Function to retrieve the token
    val tokenFlow: Flow<String?> = dataStore.data.map { preferences ->
        preferences[TOKEN_KEY]
    }

    val JWT_SECRET = "10e13609875c047a26c74fcba54bde5b"

    // Function to retrieve and decode the token
    suspend fun getUsernameFromToken(): String? {
        // Retrieve the token from the DataStoreManager
        val token = this.tokenFlow.first()

        return decodeTokenAndGetUsername(token)
    }

    fun decodeTokenAndGetUsername(token: String?): String? {
        if (token.isNullOrBlank()) {
            return "null"
        }

        try {
            val signedJWT = SignedJWT.parse(token)
            val claimsSet = signedJWT.jwtClaimsSet

            // Extract the "name" claim from the JWT claims
            return claimsSet.getClaim("name") as? String
        } catch (e: Exception) {
            // Handle any exceptions that may occur during decoding
            e.printStackTrace()
            return null
        }
    }

}
