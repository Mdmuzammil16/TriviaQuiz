package com.trivia.quiz.Api

import com.trivia.quiz.ResponseModel.QuizResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*


interface QuizApi {

    @GET("questions/")
    suspend fun getQuestions(
        @Query(value = "categories", encoded = true) category: String,
        @Query(value = "limit", encoded = true) limit: String,
        @Query(value = "difficulty", encoded = true) difficulty: String) : Response<QuizResponse>

}