package com.mazpiss.skripsi.ui.materi

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mazpiss.skripsi.R
import com.mazpiss.skripsi.ui.materiDetail.DetailMateriActivity
import com.mazpiss.skripsi.ui.materiDetail.MateriDetail

class MateriAdapter(private var userList : List<Materi>): RecyclerView.Adapter<MateriAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MateriAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.materi_item,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MateriAdapter.MyViewHolder, position: Int) {
        val materi : Materi = userList[position]
        holder.judul.text = materi.judul
        holder.totalPembahasan.text = materi.totalPembahasan
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context,DetailMateriActivity::class.java).apply {
                putExtra("urutan", materi.judul)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
       return userList.size
    }

    fun updateData(newList: List<Materi>){
        userList = newList
        notifyDataSetChanged()
    }

    public class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val judul : TextView = itemView.findViewById(R.id.materi_title_text)
        val totalPembahasan : TextView = itemView.findViewById(R.id.total_pembahasan_text)
    }
}