package com.example.buddyapp.ui.medicine

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.buddyapp.R
import com.example.buddyapp.data.local.Medicine
import com.example.buddyapp.helper.MedicineDiffCallback

class MedicineAdapter(private val listMedicine: ArrayList<Medicine>) : RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicineViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_medicine, parent, false)
        return MedicineViewHolder(view)
    }

    override fun getItemCount(): Int = listMedicine.size

    override fun onBindViewHolder(holder: MedicineViewHolder, position: Int) {
        val medicine = listMedicine[position]
        holder.medicineName.text = medicine.name
        holder.medicinePrice.text = medicine.price
        holder.medicineDescription.text = medicine.description
        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, DetailMedicineActivity::class.java).apply {
                putExtra(DetailMedicineActivity.EXTRA_NAME, medicine.name)
                putExtra(DetailMedicineActivity.EXTRA_PRICE, medicine.price)
                putExtra(DetailMedicineActivity.EXTRA_DESCRIPTION, medicine.description)
                putExtra(DetailMedicineActivity.EXTRA_URL, medicine.url)
            }
            holder.itemView.context.startActivity(intentDetail)
        }
    }

    fun updateMedicineList(listMedicine: List<Medicine>) {
        val diffCallback = MedicineDiffCallback(this.listMedicine, listMedicine)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listMedicine.clear()
        this.listMedicine.addAll(listMedicine)
        diffResult.dispatchUpdatesTo(this)
    }

    class MedicineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val medicineName: TextView = itemView.findViewById(R.id.tv_item_name)
        val medicinePrice: TextView = itemView.findViewById(R.id.tv_item_price)
        val medicineDescription: TextView = itemView.findViewById(R.id.tv_item_desc)
    }
}