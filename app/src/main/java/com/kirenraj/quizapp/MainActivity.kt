package com.kirenraj.quizapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.kirenraj.quizapp.core.service.AuthService
import com.kirenraj.quizapp.data.model.User
import com.kirenraj.quizapp.data.repo.UserRepo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var authService: AuthService

    @Inject
    lateinit var repo: UserRepo

    private val _user = MutableSharedFlow<User>()
    val user: SharedFlow<User> = _user

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.navHostFragment)

        if (authService.getCurrentUser() != null) {
            getUserRole()

            lifecycleScope.launch {
                user.collect {
                    if (it.role == "Teacher") navController.navigate(R.id.teacherHomeFragment)
                    else navController.navigate(R.id.tabContainerFragment)
                }
            }
        }
    }

    fun getUserRole() {
        lifecycleScope.launch {
            repo.getCurrentUser()?.let {
                _user.emit(it)
            }
        }
    }
}