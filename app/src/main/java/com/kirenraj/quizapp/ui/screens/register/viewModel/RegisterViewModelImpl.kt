package com.kirenraj.quizapp.ui.screens.register.viewModel

import androidx.lifecycle.viewModelScope
import com.kirenraj.quizapp.ui.screens.base.BaseViewModel

import com.kirenraj.quizapp.core.service.AuthService
import com.kirenraj.quizapp.data.model.User
import com.kirenraj.quizapp.data.repo.UserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModelImpl @Inject constructor(
    private val authService: AuthService,
    private val userRepo: UserRepo
) : BaseViewModel(), RegisterViewModel {

    override fun register(name: String, email: String, pass: String, confirmPass: String, role: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = errorHandler {
                authService.register(email, pass)
            }

            if (user != null) {
                _success.emit("Registered Successfully")
                errorHandler {
                    userRepo.addUser(
                        User(name = name, email = email, role = role)
                    )
                }
            }
        }
    }

}