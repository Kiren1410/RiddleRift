package com.kirenraj.quizapp.core.di

import com.kirenraj.quizapp.core.service.AuthService
import com.kirenraj.quizapp.data.repo.QuizRepo
import com.kirenraj.quizapp.data.repo.QuizRepoImpl
import com.kirenraj.quizapp.data.repo.ResultRepo
import com.kirenraj.quizapp.data.repo.ResultRepoImpl
import com.kirenraj.quizapp.data.repo.UserRepo
import com.kirenraj.quizapp.data.repo.UserRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepoModule {
    @Provides
    @Singleton
    fun provideUserRepo(authService: AuthService): UserRepo {
        return UserRepoImpl(authService = authService)
    }


    @Provides
    @Singleton
    fun provideQuizRepo(authService: AuthService): QuizRepo {
        return QuizRepoImpl(authService = authService)
    }

    @Provides
    @Singleton
    fun provideResultRepo(authService: AuthService): ResultRepo {
        return ResultRepoImpl(authService = authService)
    }

}

