package com.example.decotuk_app_capstone.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.decotuk_app_capstone.R
import com.example.decotuk_app_capstone.data.preferences.Record
import com.example.decotuk_app_capstone.databinding.ItemHistoryBinding

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private val listRecord = ArrayList<Record>()

    fun setData(data : ArrayList<Record>) {
        if (data.isEmpty()) return
        listRecord.clear()
        listRecord.addAll(data)
        notifyItemChanged(listRecord.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(listRecord[position])
    }

    override fun getItemCount(): Int = listRecord.size

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemHistoryBinding.bind(itemView)

        fun bind(record : Record){
            with(binding){
                tvTestDate.text = record.createdAt?.substring(0,11)
                tvTestResult.text = record.result
            }
        }
    }
}