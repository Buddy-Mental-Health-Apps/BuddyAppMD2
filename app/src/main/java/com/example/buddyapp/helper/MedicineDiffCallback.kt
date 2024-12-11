package com.example.buddyapp.helper

import androidx.recyclerview.widget.DiffUtil
import com.example.buddyapp.data.local.Medicine

class MedicineDiffCallback(
    private val oldMedicineList: List<Medicine>,
    private val newMedicineList: List<Medicine>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldMedicineList.size
    override fun getNewListSize(): Int = newMedicineList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldMedicineList[oldItemPosition].name == newMedicineList[newItemPosition].name
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldHistory = oldMedicineList[oldItemPosition]
        val newHistory = newMedicineList[newItemPosition]
        return oldHistory.imageUrl == newHistory.imageUrl && oldHistory.name == newHistory.name && oldHistory.price == newHistory.price && oldHistory.description == newHistory.description && oldHistory.url == newHistory.url
    }
}