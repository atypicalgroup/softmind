package br.com.fiap.softmind.utils

import android.content.Context
import android.content.SharedPreferences

class SurveyCache(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("SoftmindPrefs", Context.MODE_PRIVATE)

    fun setSurveyAnswered(surveyId: String) {
        prefs.edit().putBoolean("survey_$surveyId", true).apply()
    }

    fun isSurveyAnswered(surveyId: String): Boolean {
        return prefs.getBoolean("survey_$surveyId", false)
    }
}
