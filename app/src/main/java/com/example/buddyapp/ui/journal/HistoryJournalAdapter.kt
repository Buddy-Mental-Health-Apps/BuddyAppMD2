package com.example.buddyapp.ui.journal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.buddyapp.R
import com.example.buddyapp.data.local.JournalEntry

class HistoryJournalAdapter(private val history: List<JournalEntry>) : RecyclerView.Adapter<HistoryJournalAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateTextView: TextView = itemView.findViewById(R.id.tv_item_date)
        val titleTextView: TextView = itemView.findViewById(R.id.tv_item_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_journal_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.dateTextView.text = history[position].date.toString()
        holder.titleTextView.text = history[position].title
    }

    override fun getItemCount(): Int = history.size
}