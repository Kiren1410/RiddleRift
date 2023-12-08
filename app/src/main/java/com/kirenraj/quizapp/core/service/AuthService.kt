package com.kirenraj.quizapp.core.service

import com.google.firebase.auth.FirebaseUser

interface AuthService {

    suspend fun register(email: String, pass: String): FirebaseUser?

    suspend fun login(email: String, pass: String): FirebaseUser?

    fun getCurrentUser(): FirebaseUser?

    fun logout()
}