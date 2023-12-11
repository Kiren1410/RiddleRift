package com.kirenraj.quizapp.ui.screens.tabContainer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.kirenraj.quizapp.databinding.FragmentTabContainerBinding
import com.kirenraj.quizapp.ui.adapter.FragmentAdapter
import com.kirenraj.quizapp.ui.screens.profile.ProfileFragment
import com.kirenraj.quizapp.ui.screens.studentHome.StudentHomeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TabContainerFragment : Fragment() {

    private lateinit var binding: FragmentTabContainerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTabContainerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vpContainer.adapter = FragmentAdapter(
            this,
            listOf(StudentHomeFragment(), ProfileFragment())
        )

        TabLayoutMediator(binding.tlTabs, binding.vpContainer) { tab, position ->
            when(position) {
                0 -> {
                    tab.text = "Home"
                }
                1 -> {
                    tab.text = "Profile"
                }
            }
        }.attach()
    }
}