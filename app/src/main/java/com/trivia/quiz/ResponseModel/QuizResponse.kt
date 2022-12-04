package com.trivia.quiz.ResponseModel

class QuizResponse : ArrayList<QuizResponseItem>()

data class QuizResponseItem(
    val category: String,
    val correctAnswer: String,
    val difficulty: String,
    val id: String,
    val incorrectAnswers: ArrayList<String>,
    val question: String,
    val tags: List<String>,
    val type: String
)