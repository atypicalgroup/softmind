package br.com.fiap.softmind

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.fiap.softmind.screens.SplashScreen
import br.com.fiap.softmind.screens.PresentationScreen
import br.com.fiap.softmind.screens.auth.LoginScreen
import br.com.fiap.softmind.screens.administrative.AdminScreen
import br.com.fiap.softmind.screens.surveyEmployee.EmojiScreen
import br.com.fiap.softmind.screens.surveyEmployee.SuggestionScreen
import br.com.fiap.softmind.screens.surveyEmployee.SurveyScreen
import br.com.fiap.softmind.ui.theme.SoftmindTheme
import br.com.fiap.softmind.viewmodel.MoodViewModel
import br.com.fiap.softmind.viewmodel.SurveyViewModel
import br.com.fiap.softmind.viewmodel.SupportViewModel

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
                    val navController = rememberNavController()
                    var showSplashScreen by remember { mutableStateOf(true) }

                    // 🔹 ViewModels compartilhados entre telas
                    val moodViewModel: MoodViewModel = viewModel()
                    val surveyViewModel: SurveyViewModel = viewModel()
                    val supportViewModel: SupportViewModel = viewModel()

                    // 🔹 Splash inicial
                    if (showSplashScreen) {
                        SplashScreen(
                            onTimeout = { showSplashScreen = false }
                        )
                    }

                    // 🔹 Navegação principal
                    NavHost(
                        navController = navController,
                        startDestination = "PresentationScreen"
                    ) {
                        composable("PresentationScreen") {
                            PresentationScreen(navController = navController)
                        }
                        composable("LoginScreen") {
                            LoginScreen(
                                navController = navController,
                                surveyViewModel = surveyViewModel
                            )
                        }
                        composable("EmojiScreen") {
                            EmojiScreen(
                                navController = navController,
                                moodViewModel = moodViewModel,
                                surveyViewModel = surveyViewModel,
                                supportViewModel = supportViewModel
                            )
                        }
                        composable("SurveyScreen") {
                            SurveyScreen(
                                navController = navController,
                                viewModel = moodViewModel
                            )
                        }
                        composable("SuggestionScreen") {
                            SuggestionScreen(
                                navController = navController,
                                moodViewModel = moodViewModel,
                                supportViewModel = supportViewModel

                            )
                        }
                        composable("AdminScreen") {
                            AdminScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}
