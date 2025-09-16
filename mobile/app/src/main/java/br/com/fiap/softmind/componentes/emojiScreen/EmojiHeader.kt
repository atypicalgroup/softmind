package br.com.fiap.softmind.componentes.emojiScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import br.com.fiap.softmind.R
import br.com.fiap.softmind.ui.theme.BackgroundColor
import br.com.fiap.softmind.ui.theme.WelcomeColor

@Composable
fun EmojiHeader() {
    Column(
        modifier = Modifier
            .background(BackgroundColor)
    ) {
        Text(
            text = stringResource(R.string.saudacao1),
            fontSize = 30.sp,
            fontWeight = FontWeight.Medium,
            color = WelcomeColor
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.saudacao2),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
            Text(" 👋", fontSize = 28.sp)
        }
    }
}

@Preview(showBackground = true, locale = "pt-rBR")
@Composable
fun EmojiHeaderPreview() {
    EmojiHeader()
}