package com.kirenraj.quizapp.ui.screens.addQuizPage.viewModel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.kirenraj.quizapp.ui.screens.base.BaseViewModel
import com.kirenraj.quizapp.core.service.AuthService
import com.kirenraj.quizapp.data.model.Quiz
import com.kirenraj.quizapp.data.model.QuizQuestions
import com.kirenraj.quizapp.data.repo.QuizRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeacherAddQuizViewModelImpl @Inject constructor(
    private val quizRepo: QuizRepo,
    private val authService: AuthService
) : BaseViewModel(), TeacherAddQuizViewModel {

    private val _finish = MutableSharedFlow<Unit>()
    override val finish: SharedFlow<Unit> = _finish
    private val _quizQuestion = MutableStateFlow<List<QuizQuestions>>(emptyList())
    val quizQuestion: StateFlow<List<QuizQuestions>> = _quizQuestion


    override fun readCsv(lines: List<String>) {
        viewModelScope.launch {
            try {
                lines.map { line ->
                    val question = line.split(",")
                    QuizQuestions(
                        question = question[0],
                        option1 = question[1],
                        option2 = question[2],
                        option3 = question[3],
                        option4 = question[4],
                        correctAnswer = question[5]
                    )
                }.let {
                    if (it.all { true }) {
                        _quizQuestion.emit(it)
                        _success.emit(it.requireNoNulls().toString())
                        Log.d("debugging", "CSV Import Successful: ${it.size} questions imported.")
                    } else {
                        Log.e("debugging", "CSV Import Failed: Null values found in QuizQuestions.")
                    }
                }
            } catch (e: Exception) {
                Log.e("debugging", "Error parsing CSV file: ${e.message}")
            }
        }
    }

    override fun addQuiz(title: String, quizId: String, time: Long) {
       viewModelScope.launch(Dispatchers.IO) {
           val titles = mutableListOf<String>()
           val options = mutableListOf<String>()
           val answers = mutableListOf<String>()

           _quizQuestion.value.map {
               titles.add(it.question)
               options.add(it.option1)
               options.add(it.option2)
               options.add(it.option3)
               options.add(it.option4)
               answers.add(it.correctAnswer)
           }

           errorHandler {
               quizRepo.addQuiz(
                   Quiz(
                       title = title,
                       quizId = quizId,
                       titles = titles,
                       options = options,
                       answers = answers,
                       createdBy = authService.getCurrentUser()?.uid.orEmpty(),
                       timer = time
                   )
               )
               _success.emit("Quiz added successfully")
           }
           _finish.emit(Unit)
       }
    }


}