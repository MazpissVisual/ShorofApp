package com.mazpiss.skripsi.ui.quiz

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mazpiss.skripsi.databinding.ActivityQuizBinding

class QuizActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuizBinding
    private val quizViewModel:QuizViewModel by viewModels()
    private lateinit var adapter: QuizListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observeViewModel()


    }

    private fun observeViewModel() {
        quizViewModel.quizModelList.observe(this, Observer { quizModelList->
            adapter.updateDate(quizModelList)
            binding.progressBar.visibility = View.GONE
        })

        quizViewModel.loading.observe(this, Observer { isLoading->
            if (isLoading){
                binding.progressBar.visibility = View.VISIBLE
            }else{
                binding.progressBar.visibility = View.GONE
            }
        })

        quizViewModel.error.observe(this, Observer { error->
            if (error!=null){

            }
        })
    }

    private fun setupRecyclerView() {
        adapter = QuizListAdapter(emptyList())
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }



}

