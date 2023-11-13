package com.example.front.helper.hilt_modules

import com.example.front.api.Api
import com.example.front.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(api: Api): Repository {
        return Repository(api)
    }
}