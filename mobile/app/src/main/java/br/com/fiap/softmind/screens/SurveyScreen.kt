package br.com.fiap.softmind.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import br.com.fiap.softmind.R
import br.com.fiap.softmind.componentes.questions.BoxQuestions
import br.com.fiap.softmind.componentes.MindHeader
import br.com.fiap.softmind.componentes.questions.QuestionsSendButton
import br.com.fiap.softmind.viewmodel.MoodViewModel

@Composable

fun QuestionsScreen(navController: NavController, viewModel: MoodViewModel){


    data class Question(
        val text: String,
        val options: List<String>
    )

    val numericAnsewrs = listOf("5", "4", "3", "2", "1")

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

    val numericQuestion = listOf(
        R.string.pergunta_6,
        R.string.pergunta_7,
        R.string.pergunta_8,
        R.string.pergunta_9,
        R.string.pergunta_10,
        R.string.pergunta_11,
        R.string.pergunta_12,
        R.string.pergunta_13,
        R.string.pergunta_14,
        R.string.pergunta_15,
        R.string.pergunta_16,
        R.string.pergunta_17,
        R.string.pergunta_18,
        R.string.pergunta_19,
        R.string.pergunta_20,
        R.string.pergunta_21,
    ).map {
        Question(stringResource(it), numericAnsewrs)
    }

    val frequenceQuestion = listOf(
        R.string.pergunta_2,
        R.string.pergunta_3,
        R.string.pergunta_4,
        R.string.pergunta_5
    ).map {
        Question(stringResource(it), frequenceAnswers)
    }

    val lightQuestion = listOf(R.string.pergunta_1).map {
        Question(stringResource(it), lightAnswers)
    }

    val allQuestions = numericQuestion + frequenceQuestion + lightQuestion

    val dayQuestions = remember{allQuestions.shuffled().take(10)}

    var indexQuestion by remember { mutableIntStateOf(0) }

    var allQuestionsAnswered by remember { mutableStateOf(false)}

    val currentQuestion = dayQuestions.getOrNull(indexQuestion)

    Column(modifier = Modifier.fillMaxSize()) {

        Spacer(modifier = Modifier.height(60.dp))

        MindHeader()

        Spacer(modifier = Modifier.height(5.dp))

        if (currentQuestion != null){
            BoxQuestions(
                question = currentQuestion.text,
                options = currentQuestion.options,
                onSelected = {
                    if (indexQuestion < dayQuestions.size - 1){
                        indexQuestion++
                    } else {
                        allQuestionsAnswered = true
                    }
                },
                onBackQuestions = {
                    if (indexQuestion > 0){
                        indexQuestion --
                    }
                },
                currentQuestion = indexQuestion,
                totalQuestions = dayQuestions.size
            )
        }

        if (allQuestionsAnswered){
            QuestionsSendButton(onSendClick = {
                navController.navigate("EndScreen")
            })
        }

    }
}

@Preview(showBackground = true, locale = "pt-rBR")
@Composable

fun QuestionsScreenPreview(){
    QuestionsScreen(navController = NavHostController(LocalContext.current), viewModel())
}