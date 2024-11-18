package com.example.buddyapp.helper

import androidx.recyclerview.widget.DiffUtil
import com.example.buddyapp.data.local.Journal

class JournalDiffCallback(
private val oldJournalList: List<Journal>,
private val newJournalList: List<Journal>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldJournalList.size
    override fun getNewListSize(): Int = newJournalList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldJournalList[oldItemPosition].id == newJournalList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldHistory = oldJournalList[oldItemPosition]
        val newHistory = newJournalList[newItemPosition]
        return oldHistory.image == newHistory.image && oldHistory.title == newHistory.title && oldHistory.description == newHistory.description && oldHistory.timestamp == newHistory.timestamp
    }
}