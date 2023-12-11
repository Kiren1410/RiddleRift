package com.kirenraj.quizapp.data.repo

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.kirenraj.quizapp.core.service.AuthService
import com.kirenraj.quizapp.data.model.Result
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ResultRepoImpl(
    private val authService: AuthService,
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
): ResultRepo {

    private fun getDbRef(): CollectionReference {
        return db.collection("Results")
    }

    override suspend fun addResult(result: Result) {
        getDbRef().document().set(result.toHashMap()).await()
    }

    override suspend fun getResult() = callbackFlow {
        val listener = getDbRef().addSnapshotListener { value, error ->
            if (error != null) {
                throw error
            }
            val result = mutableListOf<Result>()
            value?.documents?.let { docs ->
                for (doc in docs) {
                    doc.data?.let {
                        it["id"] = doc.id
                        result.add(Result.fromHashMap(it))
                    }
                }
                trySend(result)
            }
        }
        awaitClose {
            listener.remove()
        }
    }

    override suspend fun getResultByQuizId(quizId: String) = callbackFlow {
        val listener = getDbRef().whereEqualTo("quizId", quizId).addSnapshotListener { value, error ->
            if (error != null) {
                throw error
            }
            val score = mutableListOf<Result>()
            value?.documents?.let { docs ->
                for (doc in docs) {
                    doc.data?.let {
                        it["id"] = doc.id
                        score.add(Result.fromHashMap(it))
                    }
                }
                trySend(score)
            }
        }
        awaitClose {
            listener.remove()
        }
    }
}