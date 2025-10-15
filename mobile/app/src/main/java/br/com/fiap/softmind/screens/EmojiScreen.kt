package br.com.fiap.softmind.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.fiap.softmind.R
import br.com.fiap.softmind.componentes.emojiScreen.CardSection
import br.com.fiap.softmind.componentes.emojiScreen.EmojiCardDoctor
import br.com.fiap.softmind.componentes.emojiScreen.EmojiHeader
import br.com.fiap.softmind.componentes.emojiScreen.SupportPointsSection
import br.com.fiap.softmind.ui.theme.BackgroundColor
import br.com.fiap.softmind.utils.SurveyCache
import br.com.fiap.softmind.viewmodel.MoodViewModel

@Composable
fun EmojiScreen(
    navController: NavController,
    viewModel: MoodViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val context = LocalContext.current
    val cache = remember { SurveyCache(context) }
    val surveyId = "dailySurvey"

    var alreadyAnswered by remember { mutableStateOf(cache.isSurveyAnswered(surveyId)) }

    var selectedEmoji by remember { mutableStateOf<String?>(null) }
    var selectedFeeling by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(BackgroundColor)
            .padding(top = 22.dp, start = 10.dp, end = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmojiHeader()
        Spacer(modifier = Modifier.height(16.dp))

        // PRIMEIRO CARD: estado emocional
        CardSection(
            foiVerificado = selectedEmoji != null,
            onClick = { labels -> if (!alreadyAnswered) selectedEmoji = labels },
            perguntaText = stringResource(R.string.emoji_hoje),
            perguntaFontSize = 22.sp,
            cardModifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            emojis = listOf("😌", "🥱", "😣", "😊", "😰", "😌"),
            labels = listOf(
                R.string.motivado,
                R.string.cansado,
                R.string.estressado,
                R.string.animado,
                R.string.preocupado,
                R.string.feliz
            ),
            enabled = !alreadyAnswered // 🔒 desabilita se já respondeu
        )

        Spacer(modifier = Modifier.height(16.dp))

        // SEGUNDO CARD: sentimento
        CardSection(
            foiVerificado = selectedFeeling != null,
            onClick = { feeling -> if (!alreadyAnswered) selectedFeeling = feeling },
            perguntaText = stringResource(R.string.emoji_sentir),
            perguntaFontSize = 22.sp,
            cardModifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            emojis = listOf("😔", "😟", "😠", "😄", "😱", "🥱"),
            labels = listOf(
                R.string.triste,
                R.string.ansioso,
                R.string.raiva,
                R.string.alegre,
                R.string.medo,
                R.string.cansado
            ),
            enabled = !alreadyAnswered // 🔒 desabilita se já respondeu
        )

        Spacer(modifier = Modifier.height(16.dp))

        EmojiCardDoctor(
            onClick = {
                if (!alreadyAnswered && selectedEmoji != null && selectedFeeling != null) {
                    Log.d(
                        "EMOJI_SCREEN",
                        "Enviando -> emoji: $selectedEmoji | feeling: $selectedFeeling"
                    )
                    viewModel.loadRecommendations(
                        emoji = selectedEmoji!!,
                        feeling = selectedFeeling!!
                    )
                    // ✅ salva no cache
                    cache.saveMood(surveyId, selectedEmoji!!, selectedFeeling!!)
                    cache.setSurveyAnswered(surveyId)
                    alreadyAnswered = true
                    navController.navigate("QuestionScreen")
                }
            },
            enabled = !alreadyAnswered
        )

        Spacer(modifier = Modifier.height(16.dp))

        SupportPointsSection(
            horarioFuncionamento = "Atendimento online 24h",
            nomeResponsavel = "Equipe de Apoio SoftMind",
            telefoneResponsavel = "0800 123 456"
        )
        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                if (selectedEmoji != null && selectedFeeling != null) {
                    Log.d("EMOJI_SCREEN", "Enviando para sugestões -> emoji: $selectedEmoji | feeling: $selectedFeeling")

                    viewModel.loadRecommendations(
                        emoji = selectedEmoji!!,
                        feeling = selectedFeeling!!
                    )
                    // ✅ salva no cache
                    cache.saveMood(surveyId, selectedEmoji!!, selectedFeeling!!)
                    cache.setSurveyAnswered(surveyId)
                    alreadyAnswered = true

                    // Sempre navega para EndScreen
                    navController.navigate("EndScreen")
                } else {
                    // tenta recuperar do cache caso ainda não tenha nada em memória
                    val emoji = cache.getEmoji(surveyId)
                    val feeling = cache.getFeeling(surveyId)

                    if (emoji != null && feeling != null) {
                        Log.d("EMOJI_SCREEN", "Recuperado do cache -> emoji: $emoji | feeling: $feeling")

                        viewModel.loadRecommendations(
                            emoji = emoji,
                            feeling = feeling
                        )

                        //navega para EndScreen
                        navController.navigate("EndScreen")
                    } else {
                        Log.w("EMOJI_SCREEN", "Nenhum emoji/feeling selecionado!")
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(48.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color(0xFF98FB98), Color(0xFF62BEC3))
                    ),
                    shape = RoundedCornerShape(24.dp)
                ),
        ) {
            Text(
                text = stringResource(R.string.ver_sugestoes),
                color = Color.Black,
                fontSize = 18.sp,
                letterSpacing = 0.3.sp
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@androidx.compose.ui.tooling.preview.Preview(
    showBackground = true,
    showSystemUi = false,
    locale = "pt-rBR"
)
@Composable
fun EmojiScreenPreview() {
    val navController = rememberNavController()
    EmojiScreen(navController = navController)
}
