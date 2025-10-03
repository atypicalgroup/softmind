package br.com.fiap.softmind.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import br.com.fiap.softmind.componentes.MindHeader
import br.com.fiap.softmind.componentes.questions.BoxQuestions
import br.com.fiap.softmind.componentes.questions.QuestionsSendButton
import br.com.fiap.softmind.viewmodel.SurveyViewModel

@Composable
fun QuestionsScreen(
    navController: NavController,
    viewModel: SurveyViewModel = viewModel()
) {
    val survey by viewModel.survey.collectAsState()
    val index by viewModel.currentIndex.collectAsState()
    val submitResult by viewModel.submitResult.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadDailySurvey()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        MindHeader()
        Spacer(modifier = Modifier.height(16.dp))

        if (survey == null) {
            Text("Carregando pesquisa...")
        } else {
            Text(
                text = survey!!.title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(survey!!.description)

            Spacer(modifier = Modifier.height(24.dp))

            val question = viewModel.currentQuestion

            if (question != null) {
                BoxQuestions(
                    question = question.text,
                    options = question.options,
                    onSelected = { option -> viewModel.nextQuestion(option) },
                    onBackQuestions = { viewModel.previousQuestion() },
                    currentQuestion = index,
                    totalQuestions = viewModel.totalQuestions
                )
            }

            if (viewModel.isSurveyCompleted) {
                Spacer(modifier = Modifier.height(24.dp))
                QuestionsSendButton(onSendClick = {
                    viewModel.submitAnswers()
                    navController.navigate("EndScreen")
                })
            }
        }
    }

    // Opcional: feedback de envio
    submitResult?.let {
        // Aqui você pode exibir Toast, Dialog ou Snackbar
        // Ex: Toast.makeText(context, "Respostas enviadas!", Toast.LENGTH_SHORT).show()
    }
}
