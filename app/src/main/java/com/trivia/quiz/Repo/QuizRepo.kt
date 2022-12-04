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
            Log.d("fazilApp", "moved")
        }else{
            _quizModelLiveData.value = NetworkResult.Error("Quiz Completed")
            Log.d("fazilApp", "not moved")
        }
    }

    fun onCorrectAnswer(count: Int){
        moveToNextQuestions(count)

        val previousPoint = userPreference.getUserinfo("points")

        _scoreLiveData.value = _scoreLiveData.value?.plus(1)

        val updatedPoints = previousPoint.toInt().plus(1)
        userPreference.saveUserinfo("points", updatedPoints.toString())

    }

    fun onWrongAnswer(count: Int){
        moveToNextQuestions(count)
    }


}