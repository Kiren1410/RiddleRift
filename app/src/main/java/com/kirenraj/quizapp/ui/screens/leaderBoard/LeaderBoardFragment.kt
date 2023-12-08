package com.kirenraj.quizapp.ui.screens.leaderBoard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bryan.personalproject.ui.screens.base.BaseFragment
import com.kirenraj.quizapp.databinding.FragmentLeaderBoardBinding
import com.kirenraj.quizapp.ui.adapter.ResultAdapter
import com.kirenraj.quizapp.ui.screens.leaderBoard.viewModel.LeaderBoardViewModelImpl
import com.kirenraj.quizapp.ui.screens.tabContainer.TabContainerFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class LeaderBoardFragment : BaseFragment<FragmentLeaderBoardBinding>() {

    override val viewModel: LeaderBoardViewModelImpl by viewModels()
    private lateinit var adapter: ResultAdapter
    private lateinit var categoryAdapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLeaderBoardBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun setupUiComponents(view: View) {
        super.setupUiComponents(view)
        setupAdapter()

        categoryAdapter = ArrayAdapter(
            requireContext(),
            androidx.transition.R.layout.support_simple_spinner_dropdown_item,
            emptyList()
        )

        binding.run {
            autoCompleteCategory.setOnItemClickListener { _, _, position, _ ->
                val selectedQuizId = categoryAdapter.getItem(position)
                if (!selectedQuizId.isNullOrBlank()) {
                    viewModel.getResultByQuizId(selectedQuizId)
                }
            }
            fabBack.setOnClickListener {
                val action = LeaderBoardFragmentDirections.toHome()
                navController.navigate(action)
            }
        }
    }


    override fun setupViewModelObserver(view: View) {
        super.setupViewModelObserver(view)

        lifecycleScope.launch {
            viewModel.score.collect { results ->
                // Update the ResultAdapter with the filtered and sorted list of results
                adapter.setResult(results.sortedByDescending { it.result.toIntOrNull() ?: 0 })
                // Extract unique quizIds from the results
                val quizIds = results.map { it.quizId }.distinct()
//                Log.d("LeaderBoardFragment", "Unique Quiz IDs: $quizIds")
                withContext(Dispatchers.Main) {
                    // Create a new list and update the categoryAdapter
                    val newQuizIds = ArrayList<String>(quizIds)
                    categoryAdapter = ArrayAdapter(
                        requireContext(),
                        androidx.transition.R.layout.support_simple_spinner_dropdown_item,
                        newQuizIds
                    )
                    binding.autoCompleteCategory.setAdapter(categoryAdapter)
                }
            }

        }

    }

    private fun setupAdapter() {
        adapter = ResultAdapter(emptyList())
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvLeaderBoard.adapter = adapter
        binding.rvLeaderBoard.layoutManager = layoutManager
    }

}