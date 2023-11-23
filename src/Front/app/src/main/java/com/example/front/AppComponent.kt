package com.example.front

import com.example.front.helper.hilt_modules.NetworkModule
import com.example.front.helper.hilt_modules.RepositoryModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RepositoryModule::class, NetworkModule::class])
interface AppComponent {

    fun inject(application: App)

    @Component.Builder
    interface Builder {
        fun build(): AppComponent
    }
}