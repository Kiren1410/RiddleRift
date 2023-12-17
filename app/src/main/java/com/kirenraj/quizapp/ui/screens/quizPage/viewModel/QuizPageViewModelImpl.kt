package com.kirenraj.quizapp.ui.screens.quizPage.viewModel

import android.os.CountDownTimer
import androidx.lifecycle.viewModelScope
import com.kirenraj.quizapp.ui.screens.base.BaseViewModel
import com.kirenraj.quizapp.core.service.AuthService
import com.kirenraj.quizapp.data.model.Quiz
import com.kirenraj.quizapp.data.model.Result
import com.kirenraj.quizapp.data.model.User
import com.kirenraj.quizapp.data.repo.QuizRepo
import com.kirenraj.quizapp.data.repo.ResultRepo
import com.kirenraj.quizapp.data.repo.UserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class QuizPageViewModelImpl @Inject constructor(
    private val quizRepo: QuizRepo,
    private val resultRepo: ResultRepo,
    private val authService: AuthService,
    private val userRepo: UserRepo
) : BaseViewModel(), QuizPageViewModel {

    private val _user = MutableStateFlow(User("id", "Name", "Email", "ProfileUrl", "Role"))
    val user: StateFlow<User> = _user

    private val _remainingTime = MutableStateFlow<String?>(null)
    val remainingTime: StateFlow<String?> = _remainingTime

    private val _quiz = MutableStateFlow(
        Quiz(
            titles = emptyList(),
            options = emptyList(),
            answers = emptyList(),
            createdBy = "",
            quizId = "",
            title = "",
            timer = -1
        )
    )
    val quiz: StateFlow<Quiz> = _quiz

    init {
        getCurrentUser()
    }

    override fun getQuiz(quizId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _quiz.value = errorHandler {
                quizRepo.getQuizById(quizId)
            } ?: _quiz.value // If null, keep the existing value
        }
    }

    fun startCountdownTimer(timeLimit: Long) {
        object : CountDownTimer(timeLimit * 60 * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                Timber.tag("time").d(timeLimit.toString())
                val minutes = millisUntilFinished / (60 * 1000)
                val seconds = (millisUntilFinished % (60 * 1000)) / 1000
                _remainingTime.value = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {}
        }.start()
    }

    private fun getCurrentUser() {
        authService.getCurrentUser()?.let { firebaseUser ->
            viewModelScope.launch(Dispatchers.IO) {
                _user.value = errorHandler {
                    userRepo.getUser(firebaseUser.uid)
                } ?: _user.value // If null, keep the existing value
            }
        }
    }

    fun addResult(result: String, quizId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler {
                resultRepo.addResult(Result(result=result, name = user.value.name, quizId = quizId))
            }
        }
    }
}
