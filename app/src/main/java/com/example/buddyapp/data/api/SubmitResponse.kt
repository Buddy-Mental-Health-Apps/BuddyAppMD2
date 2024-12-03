package com.example.buddyapp.data.api

import com.google.gson.annotations.SerializedName

data class SubmitResponse(

	@field:SerializedName("answers")
	val answers: List<AnswersItem?>? = null,

	@field:SerializedName("userId")
	val userId: String? = null
)

data class AnswersItem(

	@field:SerializedName("questionId")
	val questionId: Int? = null,

	@field:SerializedName("answer")
	val answer: Int? = null
)
