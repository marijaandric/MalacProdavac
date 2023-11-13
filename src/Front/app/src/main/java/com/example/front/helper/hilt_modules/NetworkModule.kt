package com.example.front.helper.hilt_modules

import com.example.front.api.Api
import com.example.front.api.RetrofitInstance
import com.example.front.helper.DataStore.DataStoreManager
import com.example.front.helper.interceptor.AuthorizationHeaderHttpClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideRetrofitInstance(httpClient: AuthorizationHeaderHttpClient): RetrofitInstance {
        return RetrofitInstance(httpClient)
    }

    @Provides
    @Singleton
    fun provideApi(retrofitInstance: RetrofitInstance): Api {
        return retrofitInstance.api
    }
}