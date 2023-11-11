package com.example.front.api
import com.example.front.helper.interceptor.AuthorizationHeaderHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

val url = com.example.front.util.Constants.BASE_URL

class RetrofitInstance @Inject constructor(private val httpClient: AuthorizationHeaderHttpClient) {

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.okHttpClient)
            .build()
    }

    val api: Api by lazy {
        retrofit.create(Api::class.java)
    }
}

