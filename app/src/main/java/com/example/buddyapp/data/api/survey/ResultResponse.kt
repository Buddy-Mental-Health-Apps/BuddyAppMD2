package com.example.buddyapp.data.api.survey

import com.google.gson.annotations.SerializedName

data class Timestamp(
	val any: Any? = null
)

data class ResultsItem(

	@field:SerializedName("answers")
	val answers: List<AnswersItem?>? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("timestamp")
	val timestamp: Timestamp? = null
)

data class AnswersItem(

	@field:SerializedName("questionId")
	val questionId: Int? = null,

	@field:SerializedName("answer")
	val answer: Int? = null
)
