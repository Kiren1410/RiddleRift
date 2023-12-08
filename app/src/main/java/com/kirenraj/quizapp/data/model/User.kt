package com.kirenraj.quizapp.data.model

data class User (
    val id: String? = null,
    val name: String,
    val email: String,
    val profileUrl: String? = "",
    val role: String
) {
    fun toHashMap(): HashMap<String, String?> {
        return hashMapOf(
            "name" to name,
            "email" to email,
            "profileUrl" to profileUrl,
            "role" to role
        )
    }

    companion object {
        fun fromHashMap(hash: Map<String, Any>): User {
            return User(
                id = hash["id"].toString(),
                name = hash["name"].toString(),
                email = hash["email"].toString(),
                profileUrl = hash["profileUrl"].toString(),
                role = hash["role"].toString()
            )
        }
    }
}

