package com.mazpiss.skripsi.ui.materiDetail

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mazpiss.skripsi.R

class SubMateriAdapter(private var subMateriList: List<MateriDetail>) : RecyclerView.Adapter<SubMateriAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.submateri_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val subMateri = subMateriList[position]
        Log.d("SubMateriAdapter", "Binding SubMateri: ${subMateri.judul}")
        holder.judul.text = subMateri.subJudul
    }

    override fun getItemCount() = subMateriList.size

    fun updateData(newList: List<MateriDetail>) {
        subMateriList = newList
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val judul: TextView = itemView.findViewById(R.id.materi_subtitle_text)
    }
}