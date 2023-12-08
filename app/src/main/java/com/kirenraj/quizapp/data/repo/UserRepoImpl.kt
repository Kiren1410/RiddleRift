package com.kirenraj.quizapp.data.repo

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

import com.kirenraj.quizapp.core.service.AuthService
import com.kirenraj.quizapp.data.model.User

import kotlinx.coroutines.tasks.await

class UserRepoImpl(
    private val authService: AuthService,
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
): UserRepo {

    private fun getDbRef(): CollectionReference {
        return db.collection("users")
    }

    private fun getUid(): String {
        val firebaseUser = authService.getCurrentUser()
        return firebaseUser?.uid ?: throw Exception("No authentic user found")
    }


    override suspend fun addUser(userRepo: User) {
        getDbRef().document(getUid()).set(userRepo.toHashMap()).await()
    }

    override suspend fun getUser(id: String): User? {
        val doc = getDbRef().document(getUid()).get().await()
        return  doc.data?.let {
            it["id"] = getUid()
            User.fromHashMap(it)
        }
    }

    override suspend fun removeUser() {
        getDbRef()
    }

    override suspend fun getCurrentUser(): User? {
        val doc = getDbRef().document(getUid()).get().await()
        return doc.data?.let {
            it["id"] = getUid()
            User.fromHashMap(it)
        }
    }

    override suspend fun updateUser(user: User) {
        getDbRef().document(getUid()).set(user.toHashMap()).await()
    }

}