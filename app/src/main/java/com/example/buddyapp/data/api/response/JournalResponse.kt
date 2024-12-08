package com.example.buddyapp.data.api.response

import com.google.gson.annotations.SerializedName

data class CreateJournalResponse(

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("userId")
	val userId: String,

	@field:SerializedName("content")
	val content: String
)

data class UpdateJournalResponse(

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("content")
	val content: String
)
