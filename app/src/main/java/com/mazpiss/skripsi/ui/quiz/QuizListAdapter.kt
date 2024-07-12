package com.mazpiss.skripsi.ui.quiz

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mazpiss.skripsi.databinding.QuizItemRecyclerRowBinding
import com.mazpiss.skripsi.ui.quizList.QuizListActivty

class QuizListAdapter(private var quizModelList: List<QuizModel>):
    RecyclerView.Adapter<QuizListAdapter.MyViewHolder>() {

    class MyViewHolder(private val binding: QuizItemRecyclerRowBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(model: QuizModel){
            binding.apply {
                quizTitleText.text = model.title
                quizSubtitleText.text = model.subtitle
                quizTimeText.text = model.time + " Min"
                root.setOnClickListener{
                    val intent = Intent(root.context, QuizListActivty::class.java)
                    QuizListActivty.questionModelList=model.questionList
                    QuizListActivty.time = model.time
                    root.context.startActivity(intent)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = QuizItemRecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return quizModelList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(quizModelList[position])
    }

    fun updateDate(newQuizModelList:List<QuizModel>){
        quizModelList = newQuizModelList
        notifyDataSetChanged()
    }
}