package com.kirenraj.quizapp.data.repo

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.kirenraj.quizapp.core.service.AuthService
import com.kirenraj.quizapp.data.model.Quiz
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class QuizRepoImpl(
    private val authService: AuthService,
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
): QuizRepo {


    private fun getDbRef(): CollectionReference {
        val firebaseUser = authService.getCurrentUser()
        return firebaseUser?.uid?.let {
            db.collection("Quizzes")
        } ?: throw Exception("No authentic user found")
    }

    override suspend fun getQuizs() = callbackFlow {
        val listener = getDbRef().addSnapshotListener { value, error ->
            if(error != null) {
                throw error
            }
            val quiz = mutableListOf<Quiz>()
            value?.documents?.let { docs ->
                for(doc in docs) {
                    doc.data?.let {
                        it["id"] = doc.id
                        quiz.add(Quiz.fromHashMap(it))
                    }
                }
                trySend(quiz)
            }
        }
        awaitClose {
            listener.remove()
        }
    }

    override suspend fun addQuiz(quiz: Quiz) {
        getDbRef().document(quiz.quizId).set(quiz.toHashMap()).await()
    }

    override suspend fun getQuizById(quizId: String): Quiz? {
        val doc = getDbRef().document(quizId).get().await()
        return doc.data?.let {
            it["id"] = doc.id
            Quiz.fromHashMap(it)
        }
    }


    override suspend fun deleteQuiz(id: String) {
        getDbRef().document(id).delete().await()
    }


}