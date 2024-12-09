package com.example.buddyapp.data.api.response

import com.google.gson.annotations.SerializedName

data class MedicineResponse(

	@field:SerializedName("listMedicine")
	val listMedicine: List<MedicineResponseItem>
)

data class MedicineResponseItem(

	@field:SerializedName("price")
	val price: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("url")
	val url: String
)

data class DetailMedicineResponse(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("price")
	val price: String,

	@field:SerializedName("url")
	val url: String,

	@field:SerializedName("imageUrl")
	val imageUrl: String,
)
