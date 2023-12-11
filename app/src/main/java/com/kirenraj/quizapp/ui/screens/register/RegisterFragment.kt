package com.kirenraj.quizapp.ui.screens.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bryan.personalproject.ui.screens.base.BaseFragment
import com.kirenraj.quizapp.R
import com.kirenraj.quizapp.databinding.FragmentRegisterBinding
import com.kirenraj.quizapp.ui.screens.register.viewModel.RegisterViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {

    override val viewModel: RegisterViewModelImpl by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUiComponents(view: View) {
        super.setupUiComponents(view)
        binding.run {
            btnRegister.setOnClickListener {
                viewModel.register(
                    etUsername.text.toString(),
                    etEmail.text.toString(),
                    etPassword.text.toString(),
                    etPassword2.text.toString(),
                    role = when (radioGroup.checkedRadioButtonId) {
                        R.id.radioButton1 -> "Teacher"
                        R.id.radioButton2 -> "Student"
                        else -> ""
                    }
                )
            }
            tvLogin.setOnClickListener {
                navController.popBackStack()
            }
        }
    }

    override fun setupViewModelObserver(view: View) {
        super.setupViewModelObserver(view)

        lifecycleScope.launch {
            viewModel.success.collect {
                val action = RegisterFragmentDirections.toLogin()
                navController.navigate(action)
            }

        }
    }

}