package br.com.fiap.softmind.componentes.emojiScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.softmind.R

@Composable
fun CardSection(
    foiVerificado: Boolean,
    onClick: (label: String) -> Unit,
    perguntaText: String,
    perguntaFontSize: TextUnit = 18.sp,
    emojis: List<String>,
    labels: List<Int>,
    cardModifier: Modifier = Modifier,
    cardBackgroundColor: Color = Color.White,
    enabled: Boolean = true // 🔑 nova flag para ativar/desativar cliques
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = cardModifier,
        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(top = 10.dp, start = 6.dp, end = 6.dp, bottom = 4.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .background(Color(0xFFD9D9D9), RoundedCornerShape(16.dp))
                    .padding(horizontal = 22.dp, vertical = 6.dp)
                    .align(Alignment.Start)
            ) {
                Text(
                    text = stringResource(R.string.botao_checagem),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF727272)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            if (!foiVerificado) {
                Text(
                    text = stringResource(R.string.emoji_toque),
                    fontSize = 16.sp,
                    color = Color(0xFF989898),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            if (foiVerificado) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(R.string.emoji_agradecimento),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 24.sp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("✅", fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = stringResource(R.string.emoji_agradecimento1),
                    fontSize = 18.sp,
                    color = Color(0xFF989898)
                )
            } else {
                Text(
                    text = perguntaText,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = perguntaFontSize
                )
                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    emojis.forEachIndexed { index, emoji ->
                        val labelText = stringResource(id = labels[index])
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(horizontal = 1.dp)
                                .let { baseModifier ->
                                    if (enabled) {
                                        baseModifier.clickable { onClick(labelText) }
                                    } else {
                                        baseModifier // 🔒 sem clickable
                                    }
                                }
                        ) {
                            Text(
                                text = emoji,
                                fontSize = 28.sp,
                                color = if (enabled) Color.Unspecified else Color.LightGray // feedback visual
                            )
                            Text(
                                text = labelText,
                                fontSize = 11.sp,
                                color = if (enabled) Color.DarkGray else Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true, locale = "pt-rBR")
@Composable
fun CardSectionPreview() {
    CardSection(
        foiVerificado = false,
        onClick = {},
        perguntaText = stringResource(R.string.emoji_hoje),
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
}
