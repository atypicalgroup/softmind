package br.com.fiap.softmind.screens.surveyEmployee

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import br.com.fiap.softmind.R
import br.com.fiap.softmind.componentes.MindHeader
import br.com.fiap.softmind.componentes.questions.BoxQuestions
import br.com.fiap.softmind.componentes.questions.QuestionsSendButton
import br.com.fiap.softmind.data.remote.model.SurveyAnswer
import br.com.fiap.softmind.viewmodel.MoodViewModel
import br.com.fiap.softmind.viewmodel.SurveyViewModel

@Composable
fun SurveyScreen(
    navController: NavController,
    viewModel: MoodViewModel,
    surveyViewModel: SurveyViewModel = viewModel()
) {
    val context = LocalContext.current

    // 🔹 Estado local das respostas
    val responses = remember { mutableStateListOf<Pair<String, String>>() }

    // 🔹 Estados de controle
    var indexQuestion by remember { mutableIntStateOf(0) }
    var allQuestionsAnswered by remember { mutableStateOf(false) }

    // 🔹 Estados do ViewModel
    val isLoading by surveyViewModel.isLoading.collectAsState()

    // 🔹 Modelagem das perguntas
    data class Question(val text: String, val options: List<String>)

    val numericAnswers = listOf("5", "4", "3", "2", "1")

    val frequenceAnswers = listOf(
        stringResource(id = R.string.nunca),
        stringResource(id = R.string.raramente),
        stringResource(id = R.string.as_vezes),
        stringResource(id = R.string.Frequentemente),
        stringResource(id = R.string.sempre)
    )

    val lightAnswers = listOf(
        stringResource(id = R.string.muito_leve),
        stringResource(id = R.string.leve),
        stringResource(id = R.string.media),
        stringResource(id = R.string.alta),
        stringResource(id = R.string.muito_alta)
    )

    val numericQuestions = listOf(
        R.string.pergunta_6, R.string.pergunta_7, R.string.pergunta_8,
        R.string.pergunta_9, R.string.pergunta_10, R.string.pergunta_11,
        R.string.pergunta_12, R.string.pergunta_13, R.string.pergunta_14,
        R.string.pergunta_15, R.string.pergunta_16, R.string.pergunta_17,
        R.string.pergunta_18, R.string.pergunta_19, R.string.pergunta_20,
        R.string.pergunta_21
    ).map { Question(stringResource(it), numericAnswers) }

    val frequenceQuestions = listOf(
        R.string.pergunta_2, R.string.pergunta_3,
        R.string.pergunta_4, R.string.pergunta_5
    ).map { Question(stringResource(it), frequenceAnswers) }

    val lightQuestions = listOf(R.string.pergunta_1).map {
        Question(stringResource(it), lightAnswers)
    }

    val allQuestions = numericQuestions + frequenceQuestions + lightQuestions
    val dayQuestions = remember { allQuestions.shuffled().take(10) }

    val currentQuestion = dayQuestions.getOrNull(indexQuestion)

    // 🔹 Layout principal
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                Spacer(modifier = Modifier.height(60.dp))
                MindHeader()
                Spacer(modifier = Modifier.height(5.dp))

                if (currentQuestion != null) {
                    BoxQuestions(
                        question = currentQuestion.text,
                        options = currentQuestion.options,
                        onSelected = { selectedOption ->
                            // 🔹 Registra resposta
                            responses.removeAll { it.first == currentQuestion.text }
                            responses.add(currentQuestion.text to selectedOption)

                            if (indexQuestion < dayQuestions.size - 1) {
                                indexQuestion++
                            } else {
                                allQuestionsAnswered = true
                            }
                        },
                        onBackQuestions = {
                            if (indexQuestion > 0) indexQuestion--
                        },
                        currentQuestion = indexQuestion,
                        totalQuestions = dayQuestions.size
                    )
                }

                // 🔹 Exibe botão de envio quando terminar
                if (allQuestionsAnswered) {
                    QuestionsSendButton(onSendClick = {
                        val surveyAnswers = responses.map { (question, answer) ->
                            SurveyAnswer(questionText = question, response = answer)
                        }

                        surveyViewModel.submitAnswers(surveyAnswers)

                        Toast.makeText(
                            context,
                            "Enviando respostas...",
                            Toast.LENGTH_SHORT
                        ).show()

                        // 🔹 Espera um pouco e navega
                        navController.navigate("SuggestionScreen") {
                            popUpTo("SurveyScreen") { inclusive = true }
                        }
                    })
                }
            }
        }
    }
}

@Preview(showBackground = true, locale = "pt-rBR")
@Composable
fun SurveyScreenPreview() {
    SurveyScreen(navController = NavHostController(LocalContext.current), viewModel())
}
