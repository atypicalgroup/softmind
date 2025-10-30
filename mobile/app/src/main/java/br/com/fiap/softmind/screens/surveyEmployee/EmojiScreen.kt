package br.com.fiap.softmind.screens.surveyEmployee

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.fiap.softmind.R
import br.com.fiap.softmind.componentes.emojiScreen.CardSection
import br.com.fiap.softmind.componentes.emojiScreen.EmojiCardDoctor
import br.com.fiap.softmind.componentes.emojiScreen.EmojiHeader
import br.com.fiap.softmind.componentes.emojiScreen.SupportPointsSection
import br.com.fiap.softmind.ui.theme.BackgroundColor
import br.com.fiap.softmind.viewmodel.MoodViewModel
import br.com.fiap.softmind.viewmodel.SupportViewModel
import br.com.fiap.softmind.viewmodel.SurveyViewModel

@Composable
fun EmojiScreen(
    navController: NavController,
    moodViewModel: MoodViewModel = viewModel(),
    surveyViewModel: SurveyViewModel = viewModel(),
    supportViewModel: SupportViewModel = viewModel()

) {
    val context = LocalContext.current

    // 🔹 Estados observados
    val survey by surveyViewModel.survey.collectAsState()
    val alreadyAnswered by surveyViewModel.alreadyAnswered.collectAsState()
    val isLoading by surveyViewModel.isLoading.collectAsState()

    // 🔹 Seleções locais
    var selectedEmoji by remember { mutableStateOf<String?>(null) }
    var selectedFeeling by remember { mutableStateOf<String?>(null) }

    val supportPoints by supportViewModel.supportPoints.collectAsState()

    // 🔹 Atualiza automaticamente ao abrir a tela
    LaunchedEffect(Unit) {
        surveyViewModel.loadDailySurvey()
    }

    // ===============================
    // 🧭 Layout principal
    // ===============================
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(BackgroundColor)
            .padding(horizontal = 10.dp, vertical = 22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmojiHeader()
        Spacer(modifier = Modifier.height(10.dp))

        if (isLoading) {
            CircularProgressIndicator(color = Color(0xFF62BEC3))
            Spacer(modifier = Modifier.height(32.dp))
        } else {
            // 🔹 Card 1: Estado emocional
            CardSection(
                foiVerificado = selectedEmoji != null,
                onClick = { if (!alreadyAnswered) selectedEmoji = it },
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
                enabled = !alreadyAnswered
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 🔹 Card 2: Sentimento
            CardSection(
                foiVerificado = selectedFeeling != null,
                onClick = { if (!alreadyAnswered) selectedFeeling = it },
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
                enabled = !alreadyAnswered
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 🔹 Botão "Ver Recomendações"
            EmojiCardDoctor(
                onClick = {
                    if (!alreadyAnswered && selectedEmoji != null && selectedFeeling != null) {
                        Log.d("EMOJI_SCREEN", "Enviando -> emoji: $selectedEmoji | feeling: $selectedFeeling")
                        moodViewModel.loadRecommendations(selectedEmoji!!, selectedFeeling!!)
                        navController.navigate("SurveyScreen")
                    }
                },
                enabled = !alreadyAnswered
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 🔹 Botão Final
            Button(
                onClick = {
                    if (!alreadyAnswered && selectedEmoji != null && selectedFeeling != null) {
                        moodViewModel.loadRecommendations(selectedEmoji!!, selectedFeeling!!)
                        navController.navigate("EndScreen")
                    } else {
                        Log.w("EMOJI_SCREEN", "Nenhum emoji/feeling selecionado!")
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
                enabled = !alreadyAnswered
            ) {
                Text(
                    text = stringResource(R.string.ver_sugestoes),
                    color = Color.Black,
                    fontSize = 18.sp,
                    letterSpacing = 0.3.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        SupportPointsSection()
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Preview(showBackground = true, locale = "pt-rBR")
@Composable
fun EmojiScreenPreview() {
    val navController = rememberNavController()
    EmojiScreen(navController = navController)
}
