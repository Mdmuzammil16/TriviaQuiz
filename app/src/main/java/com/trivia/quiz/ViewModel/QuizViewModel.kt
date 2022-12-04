package com.trivia.quiz.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trivia.quiz.Repo.QuizRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class QuizViewModel @Inject constructor(val quizRepo: QuizRepo) : ViewModel() {

    val quizListLiveData get() = quizRepo.quizListLiveData
    val quizModelLiveData get() = quizRepo.quizModelLiveData
    val quizCountLiveData get() = quizRepo.scoreLiveData

    fun getQuizQuestions(category: String,limit: String, diffulty: String){
        viewModelScope.launch {
            quizRepo.getQuizQuestions(category,limit,diffulty)
        }
    }


    fun moveToNextQuestion(count: Int){
        quizRepo.moveToNextQuestions(count)
    }

    fun onCorrect(count: Int){
        quizRepo.onCorrectAnswer(count)
    }

    fun onWrong(count: Int){
        quizRepo.onWrongAnswer(count)
    }

}