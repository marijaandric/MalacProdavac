package com.example.front.helper

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // You can choose the appropriate Hilt component
object DataStoreModule {

    @Provides
    @Singleton // You can change the scope to match your needs
    fun provideDataStoreManager(context: Context): DataStoreManager {
        return DataStoreManager(context)
    }
}
