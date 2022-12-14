package com.trivia.quiz.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.trivia.quiz.databinding.FragmentSuccessBinding
import com.trivia.quiz.utils.UserPreference
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SuccessFragment: Fragment() {

    @Inject
    lateinit var userPreference: UserPreference
    var _binding: FragmentSuccessBinding?  = null
    val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View  {

        _binding = FragmentSuccessBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.coinsTv.text = "Coins: ${userPreference.getUserinfo("points")}"
        binding.levelTv.text = "Level: ${userPreference.getUserinfo("level")}"
        binding.playBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}