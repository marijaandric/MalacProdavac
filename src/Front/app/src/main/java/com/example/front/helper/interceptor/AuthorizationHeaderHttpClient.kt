package com.example.front.helper.interceptor

import com.example.front.helper.DataStore.DataStoreManager
import okhttp3.OkHttpClient
import javax.inject.Inject

class AuthorizationHeaderHttpClient @Inject constructor(private val dataStoreManager: DataStoreManager) {

    val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(CustomInterceptor(dataStoreManager))
            .build()
    }
}