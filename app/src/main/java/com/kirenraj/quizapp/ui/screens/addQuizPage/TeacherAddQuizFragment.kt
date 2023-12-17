package com.kirenraj.quizapp.ui.screens.addQuizPage

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.kirenraj.quizapp.databinding.FragmentTeacherAddQuizBinding
import com.kirenraj.quizapp.ui.screens.addQuizPage.viewModel.TeacherAddQuizViewModelImpl
import com.kirenraj.quizapp.ui.screens.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader


@AndroidEntryPoint
class TeacherAddQuizFragment : BaseFragment<FragmentTeacherAddQuizBinding>() {
    override val viewModel: TeacherAddQuizViewModelImpl by viewModels()

    private var shouldNavigateBack = false

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val documentFile = DocumentFile.fromSingleUri(requireContext(), it)

            if (documentFile != null) {
                val originalFileName = documentFile.name
                binding.run {
                    tvCsvName.text = originalFileName.toString()
                }

                val csvFile = requireActivity().contentResolver.openInputStream(it)
                val isr = InputStreamReader(csvFile)

                BufferedReader(isr).readLines().let { lines ->
                    viewModel.readCsv(lines)
                }
            } else {
                // Handle the case when DocumentFile is null (e.g., invalid URI)
                Log.e("TeacherAddQuizFragment", "Error creating DocumentFile from URI: $it")
                // You might want to show an error message or take appropriate action
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTeacherAddQuizBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun setupUiComponents(view: View) {
        super.setupUiComponents(view)

        binding.run {
            createQuizBtn.setOnClickListener {
                val quizId = etQuizId.text.toString()
                val title = etQuizName.text.toString()
                val timer = binding.etTimer.text.toString().toLongOrNull()

                // Check if quizId, title, and CSV file are not empty before proceeding
                if (quizId.isNotBlank() && title.isNotBlank() && tvCsvName.text.isNotBlank()) {
                    if (timer != null) {
                        viewModel.addQuiz(title, quizId, timer)
                    }
                    val action = TeacherAddQuizFragmentDirections.toTeacherHome()
                    navController.navigate(action)
                } else {
                    // Show a Toast message for the error
                    Toast.makeText(
                        requireContext(),
                        "Please fill in all fields and select a CSV file",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            btnImportCsv.setOnClickListener {
                getContent.launch("text/*")
            }
        }
    }


    override fun setupViewModelObserver(view: View) {
        super.setupViewModelObserver(view)
        lifecycleScope.launch {
            viewModel.success.collect {
                if (shouldNavigateBack) {
                    navController.popBackStack()
                    shouldNavigateBack = false
                }
            }
        }

    }

}

