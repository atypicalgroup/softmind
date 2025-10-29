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

    private val _alreadyAnswered = MutableStateFlow(false)
    val alreadyAnswered: StateFlow<Boolean> = _alreadyAnswered

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    val currentQuestion: SurveyQuestion?
        get() = _survey.value?.questions?.getOrNull(_currentIndex.value)

    val totalQuestions: Int
        get() = _survey.value?.questions?.size ?: 0

    val isSurveyCompleted: Boolean
        get() = _currentIndex.value >= totalQuestions

    /**
     * 🔹 Carrega a pesquisa diária diretamente do backend
     */
    fun loadDailySurvey() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = ApiClient.surveyService.getDailySurvey()

                when (response.code()) {
                    204 -> {
                        // já respondeu hoje
                        _alreadyAnswered.value = true
                        _survey.value = null
                    }
                    200 -> {
                        val surveyData = response.body()
                        _survey.value = surveyData
                        _currentIndex.value = 0
                        _answers.value = emptyList()
                        _alreadyAnswered.value = surveyData?.alreadyAnswered ?: false
                    }
                    else -> {
                        Log.e("SURVEY", "Erro: ${response.code()} ${response.message()}")
                    }
                }
            } catch (e: Exception) {
                Log.e("SURVEY", "Falha na conexão", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Avança para a próxima pergunta e salva a resposta
     */
    fun nextQuestion(selectedAnswer: String) {
        val question = currentQuestion ?: return
        val updated = _answers.value.toMutableList().apply {
            removeAll { it.questionText == question.text }
            add(SurveyAnswer(question.text, selectedAnswer))
        }
        _answers.value = updated

        if (_currentIndex.value < totalQuestions - 1)
            _currentIndex.value++
        else
            _currentIndex.value = totalQuestions
    }

    fun previousQuestion() {
        if (_currentIndex.value > 0) _currentIndex.value--
    }

    /**
     * 🔹 Envia respostas para o backend
     */
    fun submitAnswers(answers: List<SurveyAnswer>) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val request = EmployeeDailyResponseRequest(
                    answers = answers,
                    participationDate = Instant.now().toString()
                )
                val response = ApiClient.surveyService.submitEmployeeDailyResponse(request)
                if (response.isSuccessful) {
                    _submitResult.value = response.body()
                    _alreadyAnswered.value = true
                } else {
                    Log.e("SURVEY", "Erro ao enviar respostas: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("SURVEY", "Falha na conexão", e)
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun updateSurveyStatus(answered: Boolean) {
        _alreadyAnswered.value = answered
    }

}
