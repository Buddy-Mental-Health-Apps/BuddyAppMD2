package com.example.buddyapp.data.api

import com.example.buddyapp.data.api.response.DetailMedResponseItem
import com.example.buddyapp.data.api.response.LoginResponse
import com.example.buddyapp.data.api.response.MedicineResponseItem
import com.example.buddyapp.data.api.response.QuestionResponse
import com.example.buddyapp.data.api.response.QuestionsItem
import com.example.buddyapp.data.api.response.RegisterResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
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

    @GET("drug-store/medicines")
    suspend fun getAllMedicines(): List<MedicineResponseItem>

    @GET("drug-store/medicines/anak")
    suspend fun getMedicineDetailAnak(): List<DetailMedResponseItem>

    @GET("drug-store/medicines/antiseptik")
    suspend fun getMedicineDetailAntiseptik(): List<DetailMedResponseItem>

    @GET("drug-store/medicines/asma")
    suspend fun getMedicineDetailAsma(): List<DetailMedResponseItem>

    @GET("drug-store/medicines/batuk")
    suspend fun getMedicineDetailBatuk(): List<DetailMedResponseItem>

    @GET("drug-store/medicines/betadine")
    suspend fun getMedicineDetailBetadine(): List<DetailMedResponseItem>

    @GET("drug-store/medicines/degeneratif")
    suspend fun getMedicineDetailDegeneratif(): List<DetailMedResponseItem>

    @GET("drug-store/medicines/demam")
    suspend fun getMedicineDetailDemam(): List<DetailMedResponseItem>

    @GET("drug-store/medicines/diabetes")
    suspend fun getMedicineDetailDiabetes(): List<DetailMedResponseItem>

    @GET("drug-store/medicines/diare")
    suspend fun getMedicineDetailDiare(): List<DetailMedResponseItem>

    @GET("drug-store/medicines/diet")
    suspend fun getMedicineDetailDiet(): List<DetailMedResponseItem>

    @GET("drug-store/medicines/hamil")
    suspend fun getMedicineDetailHamil(): List<DetailMedResponseItem>

    @GET("drug-store/medicines/herbal")
    suspend fun getMedicineDetailHerbal(): List<DetailMedResponseItem>

    @GET("drug-store/medicines/hidung")
    suspend fun getMedicineDetailHidung(): List<DetailMedResponseItem>

    @GET("drug-store/medicines/hipertensi")
    suspend fun getMedicineDetailHipertensi(): List<DetailMedResponseItem>

    @GET("drug-store/medicines/jantung")
    suspend fun getMedicineDetailJantung(): List<DetailMedResponseItem>

    @GET("drug-store/medicines/kapas")
    suspend fun getMedicineDetailKapas(): List<DetailMedResponseItem>

    @GET("drug-store/medicines/kecantikan")
    suspend fun getMedicineDetailKecantikan(): List<DetailMedResponseItem>

    @GET("drug-store/medicines/kolestrol")
    suspend fun getMedicineDetailKolestrol(): List<DetailMedResponseItem>

    @GET("drug-store/medicines/luka")
    suspend fun getMedicineDetailLuka(): List<DetailMedResponseItem>

    @GET("drug-store/medicines/maag")
    suspend fun getMedicineDetailMaag(): List<DetailMedResponseItem>

    @GET("drug-store/medicines/mata")
    suspend fun getMedicineDetailMata(): List<DetailMedResponseItem>

    @GET("drug-store/medicines/menstruasi")
    suspend fun getMedicineDetailMenstruasi(): List<DetailMedResponseItem>

    @GET("drug-store/medicines/menyusui")
    suspend fun getMedicineDetailMenyusui(): List<DetailMedResponseItem>

    @GET("drug-store/medicines/mual")
    suspend fun getMedicineDetailMual(): List<DetailMedResponseItem>

    @GET("drug-store/medicines/p3k")
    suspend fun getMedicineDetailP3k(): List<DetailMedResponseItem>

    @GET("drug-store/medicines/pereda nyeri")
    suspend fun getMedicineDetailPeredaNyeri(): List<DetailMedResponseItem>

    @GET("drug-store/medicines/suplemen")
    suspend fun getMedicineDetailSuplemen(): List<DetailMedResponseItem>

    @GET("drug-store/medicines/telinga")
    suspend fun getMedicineDetailTelinga(): List<DetailMedResponseItem>

    @GET("drug-store/medicines/tenggorokan")
    suspend fun getMedicineDetailTenggorokan(): List<DetailMedResponseItem>

    @GET("drug-store/medicines/tulang")
    suspend fun getMedicineDetailTulang(): List<DetailMedResponseItem>

    @GET("drug-store/medicines/vitamin")
    suspend fun getMedicineDetailVitamin(): List<DetailMedResponseItem>
}

