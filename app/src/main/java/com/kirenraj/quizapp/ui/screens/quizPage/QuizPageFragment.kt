package com.kirenraj.quizapp.ui.screens.quizPage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bryan.personalproject.ui.screens.base.BaseFragment
import com.kirenraj.quizapp.R
import com.kirenraj.quizapp.data.model.Quiz
import com.kirenraj.quizapp.data.model.QuizQuestions
import com.kirenraj.quizapp.databinding.FragmentQuizPageBinding
import com.kirenraj.quizapp.ui.screens.quizPage.viewModel.QuizPageViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class QuizPageFragment : BaseFragment<FragmentQuizPageBinding>() {

    override val viewModel: QuizPageViewModelImpl by viewModels()
    private val args: QuizPageFragmentArgs by navArgs()

    private var result = 0
    private var currentIndex = 0

    private val myQuestions = mutableListOf<QuizQuestions>()
    private var selectedAnswer = ""
    private var correctAnswer = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuizPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUiComponents(view: View) {
        super.setupUiComponents(view)

        viewModel.getQuiz(args.quizId)

        binding.btnFinish.setOnClickListener {
            val action = QuizPageFragmentDirections.toHome()
            navController.navigate(action)
        }

        binding.btnNext.setOnClickListener {
            handleAnswerSelection()
            handleQuizProgress()
        }
    }

    override fun setupViewModelObserver(view: View) {
        super.setupViewModelObserver(view)

        lifecycleScope.launch {
            viewModel.remainingTime.collect { remainingTime ->
                if (remainingTime == "00:00") {
                    handleTimerEnd()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.quiz.collect { quiz ->
                if (quiz.timer > 0L) {
                    viewModel.startCountdownTimer(quiz.timer)
                }
                myQuestions.clear()
                myQuestions.addAll(createQuizQuestions(quiz))
                if (currentIndex < myQuestions.size) {
                    nextPage(myQuestions[currentIndex])
                }
            }
        }

        lifecycleScope.launch {
            viewModel.remainingTime.collect { remainingTime ->
                binding.tvTimer.text = remainingTime
            }
        }
    }

    private fun handleTimerEnd() {
        binding.run {
            llQuestions.visibility = View.GONE
            constraintLayout2.visibility = View.GONE
            llResult.visibility = View.VISIBLE
            btnNext.visibility = View.GONE
            val timeoutText = "Oops time ran out: "
            val endResult = "$timeoutText$result/${myQuestions.size}"
            tvResult.text = endResult
            viewModel.addResult(result.toString(), args.quizId)
        }
    }

    private fun handleAnswerSelection() {
        val selectedRadioButtonId = binding.rgOptions.checkedRadioButtonId
        selectedAnswer = when (selectedRadioButtonId) {
            R.id.option1 -> binding.option1.text.toString()
            R.id.option2 -> binding.option2.text.toString()
            R.id.option3 -> binding.option3.text.toString()
            R.id.option4 -> binding.option4.text.toString()
            else -> ""
        }
        correctAnswer = myQuestions[currentIndex].correctAnswer
        if (selectedAnswer == correctAnswer) {
            result += 1
        }
    }

    private fun resetRadioButtons() {
        binding.rgOptions.clearCheck()
    }

    private fun handleQuizProgress() {
        currentIndex++
        if (currentIndex < myQuestions.size) {
            nextPage(myQuestions[currentIndex])
            resetRadioButtons() // Reset radio button state
        } else {
            showResult()
        }
    }


    private fun showResult() {
        binding.run {
            llQuestions.visibility = View.GONE
            constraintLayout2.visibility = View.GONE
            llResult.visibility = View.VISIBLE
            btnNext.visibility = View.GONE
            val greetingText = "You Scored: "
            val fullName = "$greetingText$result/${myQuestions.size}"
            tvResult.text = fullName
            viewModel.addResult(result.toString(), args.quizId)
        }
    }

    private fun createQuizQuestions(quiz: Quiz): List<QuizQuestions> {
        return quiz.titles.indices.map { i ->
            QuizQuestions(
                question = quiz.titles[i],
                option1 = quiz.options[4 * i],
                option2 = quiz.options[4 * i + 1],
                option3 = quiz.options[4 * i + 2],
                option4 = quiz.options[4 * i + 3],
                correctAnswer = quiz.answers.getOrNull(i) ?: ""
            )
        }
    }

    private fun nextPage(questions: QuizQuestions) {
        binding.run {
            val questionTitle = questions.question
            tvQuestion.text = questionTitle
            option1.text = questions.option1
            option2.text = questions.option2
            option3.text = questions.option3
            option4.text = questions.option4
        }
    }
}
