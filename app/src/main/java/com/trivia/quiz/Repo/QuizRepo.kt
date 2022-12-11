package com.trivia.quiz.Repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.staffofyuser.staffofyuser.Api.NetworkResult
import com.trivia.quiz.Api.QuizApi
import com.trivia.quiz.ResponseModel.QuizResponse
import com.trivia.quiz.ResponseModel.QuizResponseItem
import com.trivia.quiz.utils.UserPreference
import javax.inject.Inject
import kotlin.random.Random

class QuizRepo @Inject constructor(val quizApi: QuizApi,
                                   val userPreference: UserPreference) {

   private val _quizListLiveData = MutableLiveData<NetworkResult<QuizResponse>>()
    val quizListLiveData : LiveData<NetworkResult<QuizResponse>> get() = _quizListLiveData

    private val _quizModelLiveData = MutableLiveData<NetworkResult<QuizResponseItem>>()
    val quizModelLiveData : LiveData<NetworkResult<QuizResponseItem>> get() = _quizModelLiveData

    private val _scoreLiveData = MutableLiveData<Int>(0)
    val scoreLiveData: LiveData<Int> = _scoreLiveData


    suspend fun getQuizQuestions(category: String,limit: String, difficulty: String){

        _quizListLiveData.value = NetworkResult.Loading()
        val response = quizApi.getQuestions(category,limit, difficulty)
        if(response.isSuccessful && response.body() != null){

            _scoreLiveData.value = 0
            _quizListLiveData.value = NetworkResult.Success(response.body()!!)
            moveToNextQuestions(0)


        }else{
           val error = response.isSuccessful
            _quizListLiveData.value = NetworkResult.Error("Failed $error")
            Log.d("fazilApp", "failed $error")
        }

    }

    fun moveToNextQuestions(count: Int){

        if ((quizListLiveData.value?.data?.count() ?: 0) > count){
            _quizModelLiveData.value = quizListLiveData.value?.data?.get(count)
                ?.let { NetworkResult.Success(it) }
        }else{
            _quizModelLiveData.value = NetworkResult.Error("Quiz Completed")
        }
    }

    fun onCorrectAnswer(count: Int){
        moveToNextQuestions(count)

        val previousPoint = userPreference.getUserinfo("points","0")

        _scoreLiveData.value = _scoreLiveData.value?.plus(1)

        val updatedPoints = previousPoint.toInt().plus(1)
        userPreference.saveUserinfo("points", updatedPoints.toString())

    }

    fun onWrongAnswer(count: Int){
        moveToNextQuestions(count)
    }


    fun getFacts() : String{
        val listOfFacts = arrayListOf(
            "Did you know mountain goats are not in the goat family?",
            "Did you know the world’s longest pizza is a mile long?",
            "Did you know your body loses up to 8 percent of water on a flight?",
            "Did you know wind on Mars is audible?",
            "Did you know your skin sheds?",
            "Did you know trees can communicate?",
            "Did you know people rarely used to smile in photos?",
            "Did you know the longest human neck is over seven inches?",
            "Did you know the longest breath held underwater is 24:03 minutes?",
            "Did you know lions are identifiable through their whisker patterns?",
            "Did you know there’s a 50,000-word novel without the letter “E”?",
            "Did you know you can survive in space without a suit?",
            "Did you know “C” is the most common key used in pop songs?",
            "Did you know goats have emotional intelligence?",
            "Did you know Hogwarts would look like an abandoned building to Muggles?",
            "Did you know turtles snack on jellyfish tentacles?",
            "Did you know the fastest reptile is the sea turtle?"
        )

        val random = 0 until listOfFacts.size
        val randomNumber = random.random()
        return listOfFacts[randomNumber]
    }


}