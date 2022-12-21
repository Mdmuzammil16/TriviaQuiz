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

class QuizRepo @Inject constructor(private val quizApi: QuizApi, private val userPreference: UserPreference) {

    private val _quizListLiveData = MutableLiveData<NetworkResult<QuizResponse>>()
    val quizListLiveData : LiveData<NetworkResult<QuizResponse>> get() = _quizListLiveData

    private val _quizModelLiveData = MutableLiveData<NetworkResult<QuizResponseItem>>()
    val quizModelLiveData : LiveData<NetworkResult<QuizResponseItem>> get() = _quizModelLiveData

    private val _scoreLiveData = MutableLiveData(0)

    suspend fun getQuizQuestions(category: String,limit: String, difficulty: String){
        _quizListLiveData.value = NetworkResult.Loading()
        val response = quizApi.getQuestions(category,limit, difficulty)
        if(response.isSuccessful && response.body() != null){
            _scoreLiveData.value = 0
            _quizListLiveData.value = NetworkResult.Success(response.body()!!)
            moveToNextQuestions(0)
        }else{
            _quizListLiveData.value = NetworkResult.Error("Something Went Wrong")
         }
    }


   private fun moveToNextQuestions(count: Int){
        if ((quizListLiveData.value?.data?.count() ?: 0) > count){
            _quizModelLiveData.value = quizListLiveData.value?.data?.get(count)
                ?.let { NetworkResult.Success(it) }
        }else{
            _quizModelLiveData.value = NetworkResult.Error("Quiz Completed")
            when(count){
                5 ->   incrementLevel(1)
                10 -> incrementLevel(3)
                20 -> incrementLevel(5)
            }

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
        val previousPoints = userPreference.getUserinfo("points","0").toInt()
        if (previousPoints < 5){
            _quizListLiveData.value = NetworkResult.Error("No Coins")
        }else{
            val updateValue = previousPoints.minus(5)
            _scoreLiveData.value = updateValue
             userPreference.saveUserinfo("points",updateValue.toString())
            moveToNextQuestions(count)
        }
    }
    private fun incrementLevel(nextLvl: Int){
        val previousLevel = userPreference.getUserinfo("level", "0").toInt()
        val updatedLevel = previousLevel.plus(nextLvl)
        userPreference.saveUserinfo("level", updatedLevel.toString())
    }

    fun getFacts() : String{
        val listOfFacts = arrayListOf(
            "mountain goats are not in the goat family?",
            "the world’s longest pizza is a mile long?",
            "your body loses up to 8 percent of water on a flight?",
            "wind on Mars is audible?",
            "your skin sheds?",
            "trees can communicate?",
            "people rarely used to smile in photos?",
            "the longest human neck is over seven inches?",
            "the longest breath held underwater is 24:03 minutes?",
            "lions are identifiable through their whisker patterns?",
            "there’s a 50,000-word novel without the letter “E”?",
            "you can survive in space without a suit?",
            "“C” is the most common key used in pop songs?",
            "goats have emotional intelligence?",
            "Hogwarts would look like an abandoned building to Muggles?",
            "turtles snack on jellyfish tentacles?",
            "the fastest reptile is the sea turtle?"
        )

        val random = 0 until listOfFacts.size
        val randomNumber = random.random()
        return "Did you know ${listOfFacts[randomNumber]}"
    }
}