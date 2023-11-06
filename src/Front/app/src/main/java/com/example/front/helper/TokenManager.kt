package com.example.front.helper

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

class TokenManager(context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token_datastore")
    private val dataStore: DataStore<Preferences> = context.dataStore

    suspend fun saveToken(token: String) {
        try {
            val dataStoreKey = stringPreferencesKey("jwt_token")
            dataStore.edit { preferences ->
                preferences[dataStoreKey] = token
            }
        } catch (e: Exception) {
            // Handle the exception here (e.g., log or display an error message).
            e.printStackTrace()
        }
    }

    fun getTokenFlow(): Flow<String?> {
        val dataStoreKey = stringPreferencesKey("jwt_token")
        return dataStore.data.map { preferences ->
            preferences[dataStoreKey]
        }
    }

    fun getToken(): String {
        return runBlocking {
            val dataStoreKey = stringPreferencesKey("jwt_token")
            dataStore.data.map { preferences ->
                preferences[dataStoreKey]
            }.first() ?: ""
        }
    }
}
