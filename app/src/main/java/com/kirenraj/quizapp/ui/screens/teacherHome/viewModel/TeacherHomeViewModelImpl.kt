package com.kirenraj.quizapp.ui.screens.teacherHome.viewModel

import android.graphics.LinearGradient
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bryan.personalproject.ui.screens.base.viewModel.BaseViewModel
import com.kirenraj.quizapp.core.service.AuthService
import com.kirenraj.quizapp.data.model.Quiz
import com.kirenraj.quizapp.data.model.User
import com.kirenraj.quizapp.data.repo.QuizRepo
import com.kirenraj.quizapp.data.repo.UserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeacherHomeViewModelImpl @Inject constructor(
    private val quizRepo: QuizRepo,
    private val authService: AuthService,
    private val userRepo: UserRepo
) : BaseViewModel(), TeacherHomeViewModel {

    private val _user =  MutableStateFlow(User(name = "Unknown", email = "Unknown", role = "Unknown"))
    val user: StateFlow<User> = _user

    private val _profileUri = MutableStateFlow<Uri?>(null)
    val profileUri: StateFlow<Uri?> = _profileUri

    private val _finish = MutableSharedFlow<Unit>()
    val finish : SharedFlow<Unit> = _finish

    private val _quiz = MutableStateFlow(
        (1..2).map {
            Quiz(title = "title $it", quizId = "quiz ID $it", titles = emptyList(), options = emptyList(), answers = emptyList(), createdBy = "", timer = 0)
        }.toList()
    )

    override val quiz: StateFlow<List<Quiz>> = _quiz

    init {
        getQuiz()
        getCurrentUser()
        getProfilePicUri()
    }

    override fun getQuiz() {
        viewModelScope.launch(Dispatchers.IO) {
            quizRepo.getQuizs().collect {
                _quiz.value = it
            }
        }

    }


    fun getProfilePicUri() {
        viewModelScope.launch (Dispatchers.IO) {
            authService.getCurrentUser()?.uid.let{

            }

        }
    }

    fun getCurrentUser() {
        val firebaseUser = authService.getCurrentUser()
        firebaseUser?.let{
            viewModelScope.launch(Dispatchers.IO) {
               errorHandler { userRepo.getUser(it.uid) }?.let{user ->
                    _user.value = user
                }
            }
        }
    }

    fun delete(quiz: Quiz){
        viewModelScope.launch(Dispatchers.IO) {
            quizRepo.deleteQuiz(quiz.id)
        }
    }

    override fun logout() {
        viewModelScope.launch{
            errorHandler {  authService.logout() }
        }
    }
}