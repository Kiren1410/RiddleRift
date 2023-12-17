package com.kirenraj.quizapp.ui.screens.leaderBoard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kirenraj.quizapp.databinding.FragmentLeaderBoardBinding
import com.kirenraj.quizapp.ui.adapter.ResultAdapter
import com.kirenraj.quizapp.ui.screens.base.BaseFragment
import com.kirenraj.quizapp.ui.screens.leaderBoard.viewModel.LeaderBoardViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class LeaderBoardFragment : BaseFragment<FragmentLeaderBoardBinding>() {

    // ViewModel instance for this fragment
    override val viewModel: LeaderBoardViewModelImpl by viewModels()

    // Adapter for displaying the leaderboard results
    private lateinit var adapter: ResultAdapter

    // Adapter for the category dropdown menu
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
        // Set up the ResultAdapter for the RecyclerView
        setupAdapter()

        // Set up the ArrayAdapter for the category dropdown
        categoryAdapter = ArrayAdapter(
            requireContext(),
            androidx.transition.R.layout.support_simple_spinner_dropdown_item,
            emptyArray()
        )

        binding.run {
            // Handle item selection in the category dropdown
            autoCompleteCategory.setOnItemClickListener { _, _, position, _ ->
                val selectedQuizId = categoryAdapter.getItem(position)
                if (!selectedQuizId.isNullOrBlank()) {
                    // Fetch results for the selected quiz
                    viewModel.getResultByQuizId(selectedQuizId)
                }
            }

            // Handle navigation back to the home screen
            fabBack.setOnClickListener {
                val action = LeaderBoardFragmentDirections.toHome()
                navController.navigate(action)
            }
        }
    }

    override fun setupViewModelObserver(view: View) {
        super.setupViewModelObserver(view)

        lifecycleScope.launch {
            // Observe the LiveData or StateFlow containing leaderboard results
            viewModel.score.collect { results ->
                // Update the ResultAdapter with the filtered and sorted list of results
                adapter.setResult(results.sortedByDescending { it.result.toIntOrNull() ?:0 })

                // Extract unique quizIds from the results
                val quizIds = results.map { it.quizId }.distinct()

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

    // Set up the ResultAdapter for the RecyclerView
    private fun setupAdapter() {
        adapter = ResultAdapter(emptyList())
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvLeaderBoard.adapter = adapter
        binding.rvLeaderBoard.layoutManager = layoutManager
    }
}
