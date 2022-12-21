package com.trivia.quiz.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import com.trivia.quiz.R
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.staffofyuser.staffofyuser.Api.NetworkResult
import com.trivia.quiz.Adapter.DifficultyAdapter
import com.trivia.quiz.InterFaces.DifficultyInterface
import com.trivia.quiz.Models.DifficultyModel
import com.trivia.quiz.ViewModel.QuizViewModel
import com.trivia.quiz.databinding.FragmentDifficultyBinding
import com.trivia.quiz.utils.MusicClass
import com.trivia.quiz.utils.UserPreference
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DifficultyFragment : Fragment(), DifficultyInterface {

    private var _binding: FragmentDifficultyBinding? = null
    val binding get() = _binding!!
    private val quizViewModel by activityViewModels<QuizViewModel>()

    @Inject
    lateinit var userPreference: UserPreference

    @Inject
    lateinit var musicClass: MusicClass

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View  {

        _binding = FragmentDifficultyBinding.inflate(inflater, container, false)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

 
        binding.difficultyRv.layoutManager = LinearLayoutManager(requireContext())
        val list = arrayListOf<DifficultyModel>(
            DifficultyModel("Easy", "5", "1",R.drawable.easy, quizViewModel.getRandomFacts()),
            DifficultyModel("Medium", "10", "3", R.drawable.medium, quizViewModel.getRandomFacts()),
            DifficultyModel("Hard", "20", "5", R.drawable.hard, quizViewModel.getRandomFacts())
        )

        binding.profileLayout.setOnClickListener{
            findNavController().navigate(R.id.action_global_avatorFragment)
        }

        binding.difficultyRv.adapter = DifficultyAdapter(list, this, requireActivity())
    }


    override fun getDifficulty(model: DifficultyModel) {

        musicClass.startTouchMusic()
        val category = arguments?.getString("category","")
        quizViewModel.getQuizQuestions(category.toString(),
            model.questionsCount.lowercase(),model.title.lowercase())
        bindObserver()
    }

    private fun bindObserver() {
        quizViewModel.quizListLiveData.observe(viewLifecycleOwner){ response ->
            when(response){
                is NetworkResult.Success -> {
                    findNavController().navigate(R.id.action_difficultyFragment_to_quizFragment)
                }

                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(), "Failed "+response.message, Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {}
            }


        }
    }

    override fun onStart() {
        super.onStart()

        binding.pointsTv.text = userPreference.getUserinfo("points","0")
        val level = userPreference.getUserinfo("level","0")
        binding.levelTv.text = "Lvl: $level"
        binding.nameTv.text = userPreference.getUserinfo("name","")
        binding.profileIv.setImageResource(userPreference.getUserinfo("image").toInt())

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}