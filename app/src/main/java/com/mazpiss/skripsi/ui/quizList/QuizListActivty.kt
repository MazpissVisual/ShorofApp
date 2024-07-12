package com.mazpiss.skripsi.ui.quizList

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.mazpiss.skripsi.R
import com.mazpiss.skripsi.databinding.ActivityQuizListBinding
import com.mazpiss.skripsi.databinding.ScoreDialogBinding
import com.mazpiss.skripsi.ui.quiz.QuestionModel

class QuizListActivty : AppCompatActivity(),View.OnClickListener {
    companion object{
        var questionModelList : List<QuestionModel> = listOf()
        var time : String = ""
    }

    private lateinit var binding:ActivityQuizListBinding
    private val viewModel: QuizListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            btn0.setOnClickListener(this@QuizListActivty)
            btn1.setOnClickListener(this@QuizListActivty)
            btn2.setOnClickListener(this@QuizListActivty)
            btn3.setOnClickListener(this@QuizListActivty)
            nextBtn.setOnClickListener(this@QuizListActivty)
        }
        viewModel.startQuiz(questionModelList, time)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.questionModelList.observe(this, Observer { question->
            loadQuestions()
        })
        viewModel.currentQuestionIndex.observe(this, Observer { index->
            loadQuestions()
        })
        viewModel.selectedAnswer.observe(this, Observer { answer->

        })

        viewModel.timer.observe(this, Observer { timeRemaining->
            val seconds = timeRemaining/1000
            val minutes = seconds / 60
            val remainingSeconds = seconds % 60
            binding.timerIndicatorTextview.text = String.format("%02d:%02d",minutes,remainingSeconds)
        })

        viewModel.isQuizFinished.observe(this, Observer { isFinished->
            if (isFinished){
                finishedQuiz()
            }
        })
    }


    private fun loadQuestions(){
        val currentQuestionIndex = viewModel.currentQuestionIndex.value ?:0
        if (currentQuestionIndex >= questionModelList.size){
            finishedQuiz()
            return
        }
        binding.apply {
            questionIndicatorTextview.text = "Pertanyaan ${currentQuestionIndex + 1}/${questionModelList.size}"
            questionProgressIndicator.progress = ((currentQuestionIndex.toFloat() / questionModelList.size.toFloat()) * 100).toInt()
            questionTextview.text = questionModelList[currentQuestionIndex].question
            btn0.text = questionModelList[currentQuestionIndex].options[0]
            btn1.text = questionModelList[currentQuestionIndex].options[1]
            btn2.text = questionModelList[currentQuestionIndex].options[2]
            btn3.text = questionModelList[currentQuestionIndex].options[3]
        }
    }

    override fun onClick(view: View?) {

        binding.apply {
            btn0.setBackgroundColor(getColor(R.color.blue_50))
            btn1.setBackgroundColor(getColor(R.color.blue_50))
            btn2.setBackgroundColor(getColor(R.color.blue_50))
            btn3.setBackgroundColor(getColor(R.color.blue_50))
        }

        val clickedBtn = view as Button
        if (clickedBtn.id==R.id.next_btn){
            if (viewModel.selectedAnswer.value.isNullOrEmpty()){
                Toast.makeText(applicationContext,"Pilih dulu",Toast.LENGTH_SHORT).show()
                return;
            }
            viewModel.nextQuestion()
        }else{
            val selectedAnswer = clickedBtn.text.toString()
            viewModel.selectedAnswer(selectedAnswer)
            clickedBtn.setBackgroundColor(getColor(R.color.green))
        }
    }

    private fun finishedQuiz(){
        val totalQuestions = questionModelList.size
        val score = viewModel.score.value?:0
        val percentage = ((score.toFloat() / totalQuestions.toFloat())*100).toInt()
        val dialogBinding = ScoreDialogBinding.inflate(layoutInflater)
        dialogBinding.apply {
            scoreProgressIndicator.progress = percentage
            scoreProgressText.text = "$percentage %"
            if (percentage > 60) {
                scoreTitle.text = "Pencapaian yang Baik! Teruskan!"
                scoreTitle.setTextColor(getColor(R.color.green))
            } else {
                scoreTitle.text = "Tetap Semangat, Pelajari Lagi Materinya"
                scoreTitle.setTextColor(getColor(R.color.red))
            }

            scoreSubtitle.text = "$score dari $totalQuestions pertanyaan terjawab dengan benar"
            finishBtn.setOnClickListener{
                finish()
            }
        }
        AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setCancelable(false)
            .show()
    }
}