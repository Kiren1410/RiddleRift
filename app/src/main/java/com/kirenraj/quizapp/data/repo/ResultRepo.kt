package com.kirenraj.quizapp.data.repo

import kotlinx.coroutines.flow.Flow
import com.kirenraj.quizapp.data.model.Result

interface ResultRepo {
    suspend fun addResult(result: Result)
    suspend fun getResult() : Flow<List<Result>>
    suspend fun getResultByQuizId(quizId: String): Flow<List<Result>>
}