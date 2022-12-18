package com.trivia.quiz.Fragment

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.staffofyuser.staffofyuser.Api.NetworkResult
import com.trivia.quiz.R
import com.trivia.quiz.ViewModel.QuizViewModel
import com.trivia.quiz.databinding.FragmentQuizBinding
import com.trivia.quiz.databinding.LostdialogBinding
import com.trivia.quiz.utils.MusicClass
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class QuizFragment : Fragment() {
    private var _binding: FragmentQuizBinding? = null
    private var count = 0
    private val binding get() = _binding!!
    private lateinit var buttons: Array<Button>
    private val quizViewModel by activityViewModels<QuizViewModel>()


    @Inject
    lateinit var musicClass: MusicClass

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentQuizBinding.inflate(inflater, container, false)
          return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

          buttons =  arrayOf(binding.answerA, binding.answerB, binding.answerC, binding.answerD)
        bindObserver()
        bindQuizObserver()

        countDownTimer.start()

    }

    var countDownTimer = object : CountDownTimer(30000, 1000) {
         override fun onTick(millisUntilFinished: Long) {
            binding.timerTv.text = ""+millisUntilFinished / 1000
        }
        override fun onFinish() {
            lifecycleScope.launch {
                findNavController().popBackStack()
                Toast.makeText(requireContext(), "Timeout", Toast.LENGTH_SHORT)
            }

        }
    }

    private fun bindObserver() {
        quizViewModel.quizListLiveData.observe(viewLifecycleOwner){ response ->
            when(response) {
                is NetworkResult.Success -> {

                    binding.countTv.text = "${count.plus(1)} / ${response.data?.count()}"
                }
                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(), "failed " + response.message, Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {}
            }
        }
    }

    private fun bindQuizObserver() {
        quizViewModel.quizModelLiveData.observe(viewLifecycleOwner){ response ->

            when(response){
                is NetworkResult.Success -> {
                    binding.countTv.text =
                        "${count.plus(1)} / ${quizViewModel.quizListLiveData.value?.data?.count()}"

                    binding.questionTv.text = response.data?.question
                    val answers = response.data?.incorrectAnswers!!
                    val random = 0..answers.size
                    val randomNumber = random.random()
                    answers.add(randomNumber,response.data.correctAnswer)
                    setUpAnswers(answers, response.data.correctAnswer, randomNumber)
                }
                is NetworkResult.Error -> {
                    findNavController().navigate(R.id.action_quizFragment_to_successFragment2)
                    countDownTimer.cancel()
                }
                is NetworkResult.Loading -> {}


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

                    musicClass.startSuccessBeep()
                    lifecycleScope.launch {
                        delay(1000)
                        resetButtons()
                        quizViewModel.onCorrect(count)
                        countDownTimer.start()
                    }
                }else{

                    musicClass.startFailedBeep()
                    controlButtons(false)
                    buttons[answerIndex].setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    buttons[answerIndex].backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.green))
                    buttons[i].setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    buttons[i].backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.errorcolor))
                    lifecycleScope.launch {
                        delay(800)
                        showAlertDialog()

                    }
                }
            }
        }
    }


    private fun showAlertDialog(){

        countDownTimer.cancel()
        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = LostdialogBinding.inflate(inflater)




     val dialog =   MaterialAlertDialogBuilder(requireContext(),
            R.style.MyRounded_MaterialComponents_MaterialAlertDialog)
            .setView(binding.root).show()
        dialog.setCancelable(false)
        val questionsLeft = quizViewModel.quizListLiveData.value?.data?.size?.minus(count)
        Log.d("fazilApp", questionsLeft.toString())
        val message = when(questionsLeft){
            0 -> {
                "You have reached this Far. Don't Let it go."
            }
            in 1..5 -> {
                "Only $questionsLeft Question to  Unlock  Next Level."
            }
            else -> {
                "You are doing great. Keep Playing."
            }

        }

        binding.displayTv.text = message
        binding.backBtn.setOnClickListener {
            dialog.dismiss()
            countDownTimer.cancel()
            findNavController().popBackStack()
        }

        binding.continueBtn.setOnClickListener {
            dialog.dismiss()
            resetButtons()
            countDownTimer.start()
            quizViewModel.onWrong(count)
        }
    }


    private fun resetButtons(){
        buttons.forEach {
            it.backgroundTintList = null
            it.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
        }
        controlButtons(true)
    }

   private fun controlButtons(enable: Boolean){
        buttons.forEach {
            it.isEnabled  = enable
        }
    }

    override fun onStop() {
        super.onStop()
        countDownTimer.cancel()
    }

}