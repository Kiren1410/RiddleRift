package com.kirenraj.quizapp.ui.screens.teacherHome.viewModel

import com.kirenraj.quizapp.data.model.Quiz
import kotlinx.coroutines.flow.StateFlow

interface TeacherHomeViewModel {
    val quiz: StateFlow<List<Quiz>>

    fun getQuiz()

    fun logout()
}