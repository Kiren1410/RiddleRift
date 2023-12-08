package com.kirenraj.quizapp.ui.screens.quizPage.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import com.kirenraj.quizapp.data.model.Quiz
import com.kirenraj.quizapp.data.model.QuizQuestions
import kotlinx.coroutines.flow.StateFlow

interface QuizPageViewModel {

    fun getQuiz(quizId: String)


}