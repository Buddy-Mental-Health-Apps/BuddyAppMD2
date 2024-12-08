package com.example.buddyapp.ui.quiz

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.buddyapp.R
import com.example.buddyapp.data.local.QuizDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryQuizAdapter(
    private var items: MutableList<HistoryItem>,
    private val quizDao: QuizDao,
) : RecyclerView.Adapter<HistoryQuizAdapter.HistoryViewHolder>() {

    var onItemClick: ((HistoryItem) -> Unit)? = null

    inner class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateTextView: TextView = view.findViewById(R.id.history_quiz_date)
        val titleTextView: TextView = view.findViewById(R.id.result_tittle)
        val deleteIcon: ImageView = view.findViewById(R.id.img_delete_icon)

        fun bind(item: HistoryItem) {
            titleTextView.text = item.title
            dateTextView.text = item.date
            itemView.setOnClickListener {
                onItemClick?.invoke(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_quiz, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)

        holder.deleteIcon.setOnClickListener {
            // Mengakses context langsung dari parent ViewGroup
            val context = holder.itemView.context

            val dialogView = LayoutInflater.from(context).inflate(R.layout.ad_delete_test_history, null, false)
            val alertDialog = AlertDialog.Builder(context).create()

            alertDialog.setView(dialogView)

            val optionYesDelete = dialogView.findViewById<TextView>(R.id.option_yes_delete)
            val optionBackDelete = dialogView.findViewById<TextView>(R.id.option_back_delete)

            optionYesDelete.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    val quiz = quizDao.getQuizByDateAndTitle(item.date, item.title)
                    if (quiz != null) {
                        quizDao.delete(quiz)
                        Log.d("Database", "Quiz deleted successfully")

                        CoroutineScope(Dispatchers.Main).launch {
                            items.removeAt(position)
                            notifyItemRemoved(position)
                        }
                    }
                }
                alertDialog.dismiss()
            }

            optionBackDelete.setOnClickListener {
                alertDialog.dismiss()
            }

            alertDialog.show()
        }
    }


    override fun getItemCount(): Int = items.size

    // Tambahkan fungsi untuk memperbarui data
    fun updateData(newItems: List<HistoryItem>) {
        val diffResult = DiffUtil.calculateDiff(HistoryDiffCallback(items, newItems))

        items.clear()
        items.addAll(newItems)

        diffResult.dispatchUpdatesTo(this)
    }

}

data class HistoryItem(
    val date: String,
    val title: String,
    val description: String
)