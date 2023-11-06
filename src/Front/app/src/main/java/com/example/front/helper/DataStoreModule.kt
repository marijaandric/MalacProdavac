package com.example.front.helper

import android.content.Context
import androidx.datastore.dataStore
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.front.App
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

//@Module
//@InstallIn(App::class)
//object DataStoreModule {
//    @Provides
//    @Singleton
//    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
//        return context.dataSto
//    }
//}