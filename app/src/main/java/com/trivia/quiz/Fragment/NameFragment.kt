package com.trivia.quiz.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.trivia.quiz.R
import com.trivia.quiz.databinding.FragmentNameBinding
import com.trivia.quiz.utils.UserPreference
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NameFragment: Fragment() {


    var _binding: FragmentNameBinding? = null
    val binding get() = _binding!!

    @Inject
    lateinit var userPreference: UserPreference


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.saveBtn.setOnClickListener {
            val name = binding.nameEt.text.toString()
            if (name.isBlank()){
                Toast.makeText(requireContext(), "Please Enter Your Name", Toast.LENGTH_SHORT).show()
            }else{
                userPreference.saveUserinfo("name", name)
                findNavController().navigate(R.id.action_nameFragment_to_categoryFragment2)
            }
        }
    }
}