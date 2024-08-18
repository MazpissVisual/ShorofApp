package com.mazpiss.skripsi.ui.materiDetail

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.mazpiss.skripsi.R
import com.mazpiss.skripsi.ui.materiShorof.MateriShorofActivity

class SubMateriAdapter(private var subMateriList: List<MateriDetail>) :
    RecyclerView.Adapter<SubMateriAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.submateri_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val subMateri = subMateriList[position]
        Log.d("SubMateriAdapter", "Binding SubMateri: ${subMateri.judul}")
        holder.judul.text = subMateri.subJudul

        holder.itemView.setOnClickListener {
            val db = FirebaseFirestore.getInstance()
            db.collection("subMateri").whereEqualTo("subJudul", subMateri.subJudul)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val detailData = document.toObject(MateriDetail::class.java)
                        val intent =
                            Intent(holder.itemView.context, MateriShorofActivity::class.java)
                        intent.putExtra("judul", detailData?.judul)
                        intent.putExtra("subJudul", detailData?.subJudul)
                        intent.putExtra("content", detailData?.content)

                        holder.itemView.context.startActivity(intent)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("SubMateriAdapter", "get failed with ", exception)
                }
        }
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