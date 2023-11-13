package com.example.front.model.product

import com.google.gson.annotations.SerializedName

data class QuestionWithAnswer(
    @SerializedName("QuestionId") val questionId: Int,
    @SerializedName("AnswerId") val answerId: Int,
    @SerializedName("QuestionText") val questionText: String,
    @SerializedName("AnswerText") val answerText: String
)
