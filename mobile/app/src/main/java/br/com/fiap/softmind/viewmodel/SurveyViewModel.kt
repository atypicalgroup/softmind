package br.com.fiap.softmind.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.softmind.data.remote.ApiClient
import br.com.fiap.softmind.data.remote.dtos.SurveyDto
import br.com.fiap.softmind.data.remote.dtos.SurveyQuestion
import br.com.fiap.softmind.data.remote.model.EmployeeDailyResponse
import br.com.fiap.softmind.data.remote.model.EmployeeDailyResponseRequest
import br.com.fiap.softmind.data.remote.model.SurveyAnswer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.Instant

class SurveyViewModel : ViewModel() {

    private val _survey = MutableStateFlow<SurveyDto?>(null)
    val survey: StateFlow<SurveyDto?> = _survey

    private val _submitResult = MutableStateFlow<EmployeeDailyResponse?>(null)
    val submitResult: StateFlow<EmployeeDailyResponse?> = _submitResult

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex

    private val _answers = MutableStateFlow<List<SurveyAnswer>>(emptyList())
    val answers: StateFlow<List<SurveyAnswer>> = _answers

    val currentQuestion: SurveyQuestion?
        get() = _survey.value?.questions?.getOrNull(_currentIndex.value)

    val totalQuestions: Int
        get() = _survey.value?.questions?.size ?: 0

    val isSurveyCompleted: Boolean
        get() = _currentIndex.value >= totalQuestions

    // Carregar pesquisa diária
    fun loadDailySurvey() {
        viewModelScope.launch {
            try {
                val response = ApiClient.surveyService.getDailySurvey()
                if (response.isSuccessful) {
                    _survey.value = response.body()
                    _currentIndex.value = 0 // reset ao carregar nova pesquisa
                    _answers.value = emptyList()
                } else {
                    Log.e("SURVEY", "Erro: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("SURVEY", "Falha na conexão", e)
            }
        }
    }

    // Avançar para a próxima pergunta
    fun nextQuestion(selectedAnswer: String) {
        val question = currentQuestion ?: return
        val updatedAnswers = _answers.value.toMutableList().apply {
            removeAll { it.questionText == question.text }
            add(SurveyAnswer(question.text, selectedAnswer))
        }
        _answers.value = updatedAnswers

        if (_currentIndex.value < totalQuestions - 1) {
            _currentIndex.value++
        } else {
            // Finalizou todas
            _currentIndex.value = totalQuestions
        }
    }

    // Voltar para a anterior
    fun previousQuestion() {
        if (_currentIndex.value > 0) {
            _currentIndex.value--
        }
    }

    // Enviar respostas para o backend
    fun submitAnswers() {
        viewModelScope.launch {
            try {
                val request = EmployeeDailyResponseRequest(
                    answers = _answers.value,
                    participationDate = Instant.now().toString()
                )
                val response = ApiClient.surveyService.submitEmployeeDailyResponse(request)
                if (response.isSuccessful) {
                    _submitResult.value = response.body()
                } else {
                    Log.e("SURVEY", "Erro ao enviar respostas: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("SURVEY", "Falha na conexão", e)
            }
        }
    }
}
