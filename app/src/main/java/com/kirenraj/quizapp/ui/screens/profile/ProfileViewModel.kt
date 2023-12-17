package com.kirenraj.quizapp.ui.screens.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.kirenraj.quizapp.ui.screens.base.BaseViewModel
import com.google.firebase.storage.StorageException


import com.kirenraj.quizapp.core.service.AuthService
import com.kirenraj.quizapp.core.service.StorageService
import com.kirenraj.quizapp.data.model.User
import com.kirenraj.quizapp.data.repo.UserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authService: AuthService,
    private val userRepo: UserRepo,
    private val storageService: StorageService
) : BaseViewModel() {
    private val _user =  MutableStateFlow(User(name = "Unknown", email = "Unknown", role = "Unknown"))
    val user: StateFlow<User> = _user

    private val _profileUri = MutableStateFlow<Uri?>(null)
    val profileUri: StateFlow<Uri?> = _profileUri

    private val _finish = MutableSharedFlow<Unit>()
    val finish : SharedFlow<Unit> = _finish

    init {
        getCurrentUser()
        getProfilePicUri()
    }

    fun logout() {
        authService.logout()
        viewModelScope.launch{
            _finish.emit(Unit)
        }
    }


    fun getCurrentUser() {
        val firebaseUser = authService.getCurrentUser()
        firebaseUser?.let{
            viewModelScope.launch(Dispatchers.IO) {
                errorHandler { userRepo.getUser(it.uid) }?.let{user ->
                    _user.value = user
                }
            }
        }
    }

    fun getProfilePicUri() {
        viewModelScope.launch(Dispatchers.IO) {
            authService.getCurrentUser()?.uid?.let {
                _profileUri.value = storageService.getImage("$it.jpg")
            }
        }
    }

    fun updateProfilePic(uri: Uri) {
        user.value.id?.let { userId ->
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val name = "$userId.jpg"
                    storageService.addImage(name, uri)
                    getProfilePicUri()
                } catch (e: StorageException) {
                    Log.e("StorageException", "Error updating profile picture: ${e.message}", e)
                }
            }
        }
    }


}