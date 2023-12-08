package com.kirenraj.quizapp.ui.screens.login.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bryan.personalproject.ui.screens.base.viewModel.BaseViewModel
import com.kirenraj.quizapp.core.service.AuthService
import com.kirenraj.quizapp.data.model.User
import com.kirenraj.quizapp.data.repo.UserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModelImpl @Inject constructor(
    private val authService: AuthService,
    private val userRepo: UserRepo
) : BaseViewModel(), LoginViewModel {

    private val _user = MutableStateFlow(User(name = "Unknown", email = "Unknown", role = "Unknown"))
    val user: StateFlow<User> = _user


    override fun login(email: String, pass: String) {

        viewModelScope.launch(Dispatchers.IO) {
            val result = errorHandler {
                authService.login(email, pass)
            }
            if (result != null) {
                _success.emit("Logged in successfully! :D")
            }
        }

    }

    override fun getCurrentUser() {
        val firebaseUser = authService.getCurrentUser()
        Log.d("debugging", firebaseUser?.uid.toString())
        firebaseUser?.let {
            viewModelScope.launch(Dispatchers.IO) {
                errorHandler { userRepo.getUser(it.uid) }?.let {  user ->
                    Log.d("debugging", user.toString())
                    _user.value = user
                }
            }
        }
    }

}