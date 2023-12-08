package com.kirenraj.quizapp.ui.screens.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bryan.personalproject.ui.screens.base.BaseFragment
import com.bumptech.glide.Glide
import com.kirenraj.quizapp.R
import com.kirenraj.quizapp.data.model.Result
import com.kirenraj.quizapp.databinding.FragmentProfileBinding
import com.kirenraj.quizapp.ui.adapter.ResultAdapter
import com.kirenraj.quizapp.ui.screens.tabContainer.TabContainerFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    override val viewModel: ProfileViewModel by viewModels()
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var adapter: ResultAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pickMedia = registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            if (uri != null) {
                viewModel.updateProfilePic(uri)
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }
    }

    override fun setupUiComponents(view: View) {
        super.setupUiComponents(view)

        setupAdapter()

        binding.ivLogout.setOnClickListener {
            viewModel.logout()
        }

        binding.cvWrapper.setOnClickListener {
            val action = TabContainerFragmentDirections.actionTabContainerFragmentToLeaderBoardFragment()
            navController.navigate(action)
        }

        binding.icEditProfilePic.setOnClickListener {
            pickMedia.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
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
                val action = TabContainerFragmentDirections.toLogin()
                navController.navigate(action)
            }
        }

        lifecycleScope.launch {
            viewModel.profileUri.collect {
                try {
                    Glide.with(requireContext())
                        .load(it)
                        .placeholder(R.drawable.ic_profile)
                        .into(binding.ivProfile)
                } catch (e: Exception) {
                    Log.e("GlideException", "Error loading profile picture: ${e.message}", e)
                }
            }
        }

    }


    private fun setupAdapter() {
        adapter = ResultAdapter(emptyList())
        adapter.listener = object : ResultAdapter.Listener {
            override fun onClick(result: Result) {
                val action =
                    TabContainerFragmentDirections.actionTabContainerFragmentToLeaderBoardFragment()
                navController.navigate(action)
            }
        }
    }

}


