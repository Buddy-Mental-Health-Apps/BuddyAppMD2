package com.example.buddyapp.data.local

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = "medicine")
@Parcelize
data class Medicine(
//    @PrimaryKey(autoGenerate = true)
//    @field:SerializedName("medicineId")
//    val medicineId: Int,

    @PrimaryKey
    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("price")
    val price: String?,

    @field:SerializedName("url")
    val url: String?,

    @field:SerializedName("description")
    val description: String?

) : Parcelable