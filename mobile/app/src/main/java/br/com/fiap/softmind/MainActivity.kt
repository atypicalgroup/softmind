package br.com.fiap.softmind

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.fiap.softmind.data.utils.MoodStatusManager
import br.com.fiap.softmind.screens.AdminScreen
import br.com.fiap.softmind.screens.EmojiScreen
import br.com.fiap.softmind.screens.EndScreen
import br.com.fiap.softmind.screens.LoginScreen
import br.com.fiap.softmind.screens.QuestionsScreen
import br.com.fiap.softmind.screens.SplashScreen
import br.com.fiap.softmind.screens.PresentationScreen
import br.com.fiap.softmind.ui.theme.SoftmindTheme
import br.com.fiap.softmind.viewmodel.MoodViewModel
import br.com.fiap.softmind.viewmodel.MoodViewModelFactory
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SoftmindTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = LocalContext.current

                    val moodStatusManager = remember { MoodStatusManager(context) }
                    val factory = remember { MoodViewModelFactory(moodStatusManager) }

                    var showSplashScreen by remember { mutableStateOf(true) }
                    val navController = rememberNavController()

                    val sharedMoodViewModel: MoodViewModel = viewModel(factory = factory)

                    val hasAnsweredToday = sharedMoodViewModel.hasSubmittedToday()
                    val mainStartDestination = if (hasAnsweredToday) {
                        "EndScreen"
                    } else {
                        "PresentationScreen"
                    }


                    if (showSplashScreen) {
                        SplashScreen(onTimeout = { showSplashScreen = false })
                    }
                    NavHost(
                        navController = navController,
                        startDestination = mainStartDestination
                    ) {
                        composable("PresentationScreen") { PresentationScreen(navController = navController) }
                        composable("LoginScreen") { LoginScreen(navController = navController) }
                        composable("EmojiScreen") { EmojiScreen(navController = navController, viewModel = sharedMoodViewModel) }
                        composable("AdminScreen") { AdminScreen(navController = navController) }
                        composable("QuestionScreen") { QuestionsScreen(navController = navController, viewModel = sharedMoodViewModel) }
                        composable("EndScreen") { EndScreen(navController = navController, viewModel = sharedMoodViewModel) }
                    }
                }
            }
        }
    }
}