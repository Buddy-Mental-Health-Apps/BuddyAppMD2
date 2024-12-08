package com.example.buddyapp.data.api

import com.example.buddyapp.data.api.response.AnswersItem
import com.example.buddyapp.data.api.response.CreateJournalResponse
import com.example.buddyapp.data.api.response.LoginResponse
import com.example.buddyapp.data.api.response.QuestionResponse
import com.example.buddyapp.data.api.response.QuestionsItem
import com.example.buddyapp.data.api.response.RegisterResponse
import com.example.buddyapp.data.api.response.SubmitResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("auth/register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("survey/questions")
    suspend fun question(
        @Query("questions") question: List<QuestionsItem>,
    ): QuestionResponse

    @POST("survey/submit")
    suspend fun submit(
        @Part("userId") userId: String,
        @Part("answers") answers: List<AnswersItem>
    ): SubmitResponse

    @POST("journal")
    suspend fun createJournal(
        @Part("userId") userId: String,
        @Part("title") title: String,
        @Part("content") content: String
    ): CreateJournalResponse

    @GET("journal/user/")
    suspend fun getJournal(
        @Query("userId") userId: String
    ): List<CreateJournalResponse>
}

