package com.kirenraj.quizapp.core.di

import com.google.firebase.firestore.FirebaseFirestore
import com.kirenraj.quizapp.core.service.AuthService
import com.kirenraj.quizapp.core.service.AuthServiceImpl
import com.kirenraj.quizapp.core.service.StorageService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideAuthService(): AuthService {
        return AuthServiceImpl()
    }

    @Provides
    @Singleton
    fun provideStorageService(): StorageService {
        return StorageService()
    }

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }
}