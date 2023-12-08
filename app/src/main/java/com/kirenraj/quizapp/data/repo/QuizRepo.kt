package com.kirenraj.quizapp.data.repo

import com.kirenraj.quizapp.data.model.Quiz
import kotlinx.coroutines.flow.Flow

interface QuizRepo {

    suspend fun getQuizs(): Flow<List<Quiz>>

    suspend fun addQuiz(quiz: Quiz)

    suspend fun getQuizById(quizId: String): Quiz?


    suspend fun deleteQuiz(id: String)




}