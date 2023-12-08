package com.kirenraj.quizapp.ui.screens.studentHome.viewModel


import androidx.lifecycle.viewModelScope
import com.bryan.personalproject.ui.screens.base.viewModel.BaseViewModel
import com.kirenraj.quizapp.core.service.AuthService
import com.kirenraj.quizapp.data.model.Quiz
import com.kirenraj.quizapp.data.repo.QuizRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class StudentHomeViewModelImpl @Inject constructor(
    private val quizRepo: QuizRepo,
    private val authService: AuthService
) : BaseViewModel(), StudentHomeViewModel {

    override fun getQuiz(id: String) {
        viewModelScope.launch {
            try {
                val quiz = quizRepo.getQuizById(id)
            } catch (e: Exception) {
                errorHandler {
                    _error.emit("Please Input A Valid Quiz ID")
                }
            }
        }
    }

    override fun logout() {
        authService.logout()
    }
}
