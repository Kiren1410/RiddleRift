package com.bryan.personalproject.ui.screens.base

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.viewbinding.ViewBinding
import com.bryan.personalproject.ui.screens.base.viewModel.BaseViewModel
import com.google.android.material.snackbar.Snackbar
import com.kirenraj.quizapp.R
import kotlinx.coroutines.launch

abstract class BaseFragment<T:ViewBinding> : Fragment() {


    abstract val viewModel: BaseViewModel
    lateinit var binding : T
    protected lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onCreate()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = NavHostFragment.findNavController(this)
        setupUiComponents(view)
        setupViewModelObserver(view)
    }

    protected open fun onFragmentResult() {}
    open fun setupUiComponents(view: View) {}

    open fun setupViewModelObserver(view: View) {
        lifecycleScope.launch {
            viewModel.error.collect {
                showSnackbar(view, it)
            }
        }
        lifecycleScope.launch {
            viewModel.success.collect {
                showSnackbar(view, it)
            }
        }
    }

    fun showSnackbar(view: View, msg: String, isError: Boolean = false) {

        val fragmentView = view
        if (fragmentView  != null && isAdded && !isDetached) {

            val snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG)

            if (isError) {
                snackbar.setBackgroundTint(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.error
                    )
                )
            } else {
                snackbar.setBackgroundTint(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.main
                    )
                ).setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    )
                )
            }
            snackbar.show()

        } else {
            Log.e("SnackbarError", "Cannot show Snackbar: Fragment is not attached or view is null.")
        }


    }

    fun logMsg(msg: String, tag: String = "debugging") {
        Log.d(tag, msg)
    }

}