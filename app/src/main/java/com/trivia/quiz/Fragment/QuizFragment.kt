package com.trivia.quiz.Fragment

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.staffofyuser.staffofyuser.Api.NetworkResult
import com.trivia.quiz.R
import com.trivia.quiz.ViewModel.QuizViewModel
import com.trivia.quiz.databinding.FragmentQuizBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class QuizFragment : Fragment() {
    var _binding: FragmentQuizBinding? = null
    var count = 0
    val binding get() = _binding!!
    lateinit var buttons: Array<Button>
    val quizViewModel by activityViewModels<QuizViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentQuizBinding.inflate(inflater, container, false)
          return   binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

      buttons =  arrayOf(binding.answerA, binding.answerB, binding.answerC, binding.answerD)
        bindObserver()
        bindQuizObserver()

    }



    private fun bindObserver() {
        quizViewModel.quizListLiveData.observe(viewLifecycleOwner){ response ->
            when(response){
                is NetworkResult.Success -> {
                    binding.countTv.text = "$count / ${response.data?.count()}"
                }
                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(), "failed "+response.message, Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {}

            }

        }
    }

    private fun bindQuizObserver() {
        quizViewModel.quizModelLiveData.observe(viewLifecycleOwner){ response ->

            when(response){
                is NetworkResult.Success -> {
                    binding.questionTv.text = response.data?.question
                    val answers = response.data?.incorrectAnswers!!
                    val random = 0..answers.size
                    val randomNumber = random.random()
                    answers.add(randomNumber,response.data.correctAnswer)
                    setUpAnswers(answers,
                        response.data.correctAnswer, randomNumber)
                }
                is NetworkResult.Error -> {
                    findNavController().navigate(R.id.action_quizFragment_to_successFragment2)
                }


            }



        }
    }

    private fun setUpAnswers(answers: ArrayList<String>, correctAnswer: String, answerIndex: Int) {

        for (i in 0..3){
            buttons[i].backgroundTintList = null
            buttons[i].text = answers[i]
            buttons[i].setOnClickListener{
                count = count.plus(1)
                if (buttons[i].text.equals(correctAnswer)){
                    buttons[i].setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    buttons[i].backgroundTintList = ColorStateList.valueOf(Color.parseColor("#159f8b"))
                    controlButtons(false)
                    Handler(Looper.myLooper()!!).postDelayed({
                          resetButtons()
                        quizViewModel.onCorrect(count)
                    }, 500)

                }else{
                    controlButtons(false)

                    buttons[answerIndex].setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    buttons[answerIndex].backgroundTintList = ColorStateList.valueOf(Color.parseColor("#159f8b"))
                    buttons[i].setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    buttons[i].backgroundTintList = ColorStateList.valueOf(Color.parseColor("#e94d4e"))

                    Handler(Looper.myLooper()!!).postDelayed({
                        resetButtons()
                        quizViewModel.onWrong(count)
                    }, 500)

                }
            }
        }
    }


    fun resetButtons(){
        buttons.forEach { it ->
            it.backgroundTintList = null
            it.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
        }
        binding.countTv.text = "$count / ${quizViewModel.quizListLiveData.value?.data?.count()}"
        controlButtons(true)
    }

    fun controlButtons(enable: Boolean){
        buttons.forEach {
            it.isEnabled  = enable
        }
    }

}