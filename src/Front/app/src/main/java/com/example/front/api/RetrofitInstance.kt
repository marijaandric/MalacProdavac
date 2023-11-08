package com.example.front.api
import com.example.front.helper.DataStore.DataStoreManager
import com.example.front.helper.interceptor.AuthorizationHeaderHttpClient
import com.example.front.util.Contsnats
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

val url = com.example.front.util.Contsnats.BASE_URL

class RetrofitInstance @Inject constructor(private val httpClient: AuthorizationHeaderHttpClient) {

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.okHttpClient) // Use the injected OkHttpClient with the authorization header interceptor
            .build()
    }

    val api: Api by lazy {
        retrofit.create(Api::class.java)
    }
}

