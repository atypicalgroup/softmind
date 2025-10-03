package br.com.fiap.softmind.data.utils

import android.content.Context
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class MoodStatusManager(context: Context) {

    private val prefs = context.applicationContext.getSharedPreferences("mood_prefs", Context.MODE_PRIVATE)
    private val KEY_LAST_SUBMISSION_DATE = "last_submission_date"
    private val DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE // Ex: 2025-10-03

    fun getLastSubmissionDate(): LocalDate? {
        val dateString = prefs.getString(KEY_LAST_SUBMISSION_DATE, null)
        return dateString?.let {
            try {
                LocalDate.parse(it, DATE_FORMATTER)
            } catch (e: DateTimeParseException) {
                null // Retorna null se a string for inválida
            }
        }
    }

    fun saveSubmissionDate() {
        val today = LocalDate.now().format(DATE_FORMATTER)
        prefs.edit().putString(KEY_LAST_SUBMISSION_DATE, today).apply()
    }
}