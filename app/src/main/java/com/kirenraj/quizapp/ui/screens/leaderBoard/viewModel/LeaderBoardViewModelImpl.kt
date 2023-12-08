package com.kirenraj.quizapp.ui.screens.leaderBoard.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bryan.personalproject.ui.screens.base.viewModel.BaseViewModel
import com.kirenraj.quizapp.data.model.Result
import com.kirenraj.quizapp.data.repo.ResultRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaderBoardViewModelImpl @Inject constructor(
    private val resultRepo: ResultRepo
) : BaseViewModel(), LeaderBoardViewModel {
    private val _score: MutableStateFlow<List<Result>> = MutableStateFlow(emptyList())
    override val score: StateFlow<List<Result>> = _score


    init {
        getScore()
    }

    override fun getScore() {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler {
                resultRepo.getResult()
            }?.collect {
                _score.value = it
            }
        }
    }

    override fun getResultByQuizId(quizId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler {
                resultRepo.getResultByQuizId(quizId)
            }?.collect {
//                Log.d("LeaderBoardViewModelImpl", "Scores by Quiz ID: $it")
                _score.value = it
            }
        }
    }
}