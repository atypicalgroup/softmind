package br.com.fiap.softmind.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.fiap.softmind.data.utils.MoodStatusManager

// 🟢 A Factory sabe como construir o MoodViewModel passando o MoodStatusManager
class MoodViewModelFactory(
    private val statusManager: MoodStatusManager
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MoodViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MoodViewModel(statusManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}