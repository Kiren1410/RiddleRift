package com.kirenraj.quizapp.data.repo

import com.kirenraj.quizapp.data.model.User


interface UserRepo {

    suspend fun addUser(userRepo: User)

    suspend fun getUser(id: String): User?

    suspend fun removeUser()

    suspend fun getCurrentUser(): User?

    suspend fun updateUser(user: User)

}
