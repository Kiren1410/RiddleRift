package com.kirenraj.quizapp.ui.screens.studentHome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.kirenraj.quizapp.databinding.FragmentStudentHomeBinding
import com.kirenraj.quizapp.ui.screens.base.BaseFragment
import com.kirenraj.quizapp.ui.screens.studentHome.viewModel.StudentHomeViewModelImpl
import com.kirenraj.quizapp.ui.screens.tabContainer.TabContainerFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StudentHomeFragment : BaseFragment<FragmentStudentHomeBinding>() {
    override val viewModel: StudentHomeViewModelImpl by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStudentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun setupUiComponents(view: View) {
        super.setupUiComponents(view)

        binding.run {

            btnStart.setOnClickListener {
                val id = etQuizId.text.toString()
                if (id.isEmpty()) {
                    Snackbar.make(
                        requireView(),
                        "Quiz ID cannot be empty",
                        Snackbar.LENGTH_LONG
                    ).show()
                } else {
                    val action = TabContainerFragmentDirections.actionTabContainerFragmentToQuizPageFragment(id)
                    navController.navigate(action)
                }
            }

        }

    }


}
