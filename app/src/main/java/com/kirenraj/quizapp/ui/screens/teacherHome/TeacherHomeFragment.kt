package com.kirenraj.quizapp.ui.screens.teacherHome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kirenraj.quizapp.data.model.Quiz
import com.kirenraj.quizapp.databinding.FragmentTeacherHomeBinding
import com.kirenraj.quizapp.ui.adapter.QuizAdapter
import com.kirenraj.quizapp.ui.screens.base.BaseFragment
import com.kirenraj.quizapp.ui.screens.teacherHome.viewModel.TeacherHomeViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TeacherHomeFragment : BaseFragment<FragmentTeacherHomeBinding>() {

    override val viewModel: TeacherHomeViewModelImpl by viewModels()

    private lateinit var adapter: QuizAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTeacherHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getQuiz()
    }

    override fun setupUiComponents(view: View) {
        super.setupUiComponents(view)
        setupAdapter()

        binding.fabLogout.setOnClickListener {
            viewModel.logout()
        }

        binding.fabAdd.setOnClickListener {
            val action = TeacherHomeFragmentDirections.actionTeacherHomeToTeacherAddQuizFragment()
            navController.navigate(action)
        }


    }

    override fun setupViewModelObserver(view: View) {
        super.setupViewModelObserver(view)

        lifecycleScope.launch {
            viewModel.user.collect {
                binding.tvEmail.text = it.email
                binding.tvUsername.text = it.name

            }
        }

        lifecycleScope.launch {
            viewModel.finish.collect {
                val action = TeacherHomeFragmentDirections.toLogin2()
                navController.navigate(action)
            }
        }



        lifecycleScope.launch {
            viewModel.quiz.collect {
                adapter.setQuiz(it)
            }
        }

        binding.fabLogout.setOnClickListener {
            viewModel.logout()
            val action = TeacherHomeFragmentDirections.toLogin()
            navController.navigate(action)
        }


    }



    private fun setupAdapter() {
        adapter = QuizAdapter(emptyList())

        adapter.listener = object : QuizAdapter.Listener {

            override fun onDelete(quiz: Quiz) {
                viewModel.delete(quiz)
            }

        }
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvQuiz.adapter = adapter
        binding.rvQuiz.layoutManager = layoutManager
    }
}