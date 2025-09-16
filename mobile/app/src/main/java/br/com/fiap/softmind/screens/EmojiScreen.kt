package br.com.fiap.softmind.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.fiap.softmind.R
import br.com.fiap.softmind.componentes.emojiScreen.CardSection
import br.com.fiap.softmind.componentes.emojiScreen.EmojiCardDoctor
import br.com.fiap.softmind.componentes.emojiScreen.EmojiHeader
import br.com.fiap.softmind.ui.theme.BackgroundColor

@Composable
fun EmojiScreen(navController: NavController) {
    var emojiSelecionado1 by remember { mutableStateOf(false) }
    var emojiSelecionado2 by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(top = 22.dp, start = 10.dp, end = 10.dp)
    ) {
        EmojiHeader()

        Spacer(modifier = Modifier.height(12.dp))

        CardSection(
            foiVerificado = emojiSelecionado1,
            onClick = { emojiSelecionado1 = true },
            perguntaText = stringResource(R.string.emoji_hoje),
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
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        CardSection(
            foiVerificado = emojiSelecionado2,
            onClick = { emojiSelecionado2 = true },
            perguntaText = stringResource(R.string.emoji_sentir),
            perguntaFontSize = 22.sp,
            cardModifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            emojis = listOf("😌", "🥱", "😣", "😊", "😰", "😌"),
            labels = listOf(
                R.string.motivado,
                R.string.cansado,
                R.string.estressado,
                R.string.Animado,
                R.string.preocupado,
                R.string.Feliz
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        EmojiCardDoctor(
            onClick = {
                navController.navigate("QuestionScreen")
            }
        )
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true,
    showSystemUi = false, locale = "pt-rBR"
)
@Composable
fun EmojiScreenPreview() {
    val navController = rememberNavController()
    EmojiScreen(navController = navController)
}