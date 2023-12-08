package com.kirenraj.quizapp.ui.screens.login.viewModel

interface LoginViewModel {

    fun login(email: String, pass: String)
    fun getCurrentUser()

}