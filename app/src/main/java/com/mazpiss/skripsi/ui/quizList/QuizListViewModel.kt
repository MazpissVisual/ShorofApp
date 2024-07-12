package com.mazpiss.skripsi.ui.quizList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mazpiss.skripsi.ui.quiz.QuestionModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuizListViewModel :ViewModel() {
    private val _questionModelList = MutableLiveData<List<QuestionModel>>()
    val questionModelList : LiveData<List<QuestionModel>> get() = _questionModelList

    private val _currentQuestionIndex = MutableLiveData<Int>()
    val currentQuestionIndex:LiveData<Int> get() = _currentQuestionIndex

    private val _selectedAnswer = MutableLiveData<String>()
    val selectedAnswer: LiveData<String>get() = _selectedAnswer

    private val _score = MutableLiveData<Int>()
    val score:LiveData<Int>get() = _score

    private val _timer = MutableLiveData<Long>()
    val timer : LiveData<Long>get() = _timer

    private val _isQuizFinished = MutableLiveData<Boolean>()
    val isQuizFinished : LiveData<Boolean>get() = _isQuizFinished

    init {
        _currentQuestionIndex.value = 0
        _selectedAnswer.value = ""
        _score.value = 0
        _isQuizFinished.value = false
    }

    fun startQuiz(questions:List<QuestionModel>,time:String){
        _questionModelList.value = questions
        startTime(time.toInt())
    }

    private fun startTime(totalTimeInMinutes: Int) {
        val totalTimeInMills = totalTimeInMinutes * 60 * 1000L
        viewModelScope.launch (Dispatchers.Main ){
            for (time in totalTimeInMills downTo 0 step 1000L){
                _timer.value = time
                kotlinx.coroutines.delay(1000L)
            }
            _isQuizFinished.value = true
        }
    }

    fun selectedAnswer(answer:String){
        _selectedAnswer.value = answer
    }

    fun nextQuestion(){
        if (_selectedAnswer.value == questionModelList.value?.get(_currentQuestionIndex.value!!)?.correct){
            _score.value = _score.value?.plus(1)
        }
        if (_currentQuestionIndex.value!! <questionModelList.value!!.size-1){
            _currentQuestionIndex.value = _currentQuestionIndex.value?.plus(1)
            _selectedAnswer.value = ""
        }else{
            _isQuizFinished.value = true
        }
    }
}