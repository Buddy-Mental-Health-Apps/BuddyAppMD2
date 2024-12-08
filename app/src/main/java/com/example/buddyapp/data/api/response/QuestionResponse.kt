package com.example.buddyapp.data.api.response

import com.google.gson.annotations.SerializedName

data class QuestionResponse(

	@field:SerializedName("questions")
	val questions: List<QuestionsItem?>? = null
)

data class Answers(

	@field:SerializedName("0")
	val jsonMember0: String? = null,

	@field:SerializedName("1")
	val jsonMember1: String? = null,

	@field:SerializedName("2")
	val jsonMember2: String? = null,

	@field:SerializedName("3")
	val jsonMember3: String? = null,

	@field:SerializedName("4")
	val jsonMember4: String? = null
)

data class QuestionsItem(

    @field:SerializedName("question")
	val question: String? = null,

    @field:SerializedName("answers")
	val answers: Answers? = null,

    @field:SerializedName("id")
	val id: Int? = null
)
