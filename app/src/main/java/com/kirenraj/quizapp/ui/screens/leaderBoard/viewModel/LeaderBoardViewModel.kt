package com.kirenraj.quizapp.ui.screens.leaderBoard.viewModel

import com.kirenraj.quizapp.data.model.Result
import kotlinx.coroutines.flow.StateFlow

interface LeaderBoardViewModel {
    val score: StateFlow<List<Result>>

    fun getScore()
    fun getResultByQuizId(quizId: String)
}
