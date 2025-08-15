package br.com.fiap.softmind.componentes.presentationScreen

import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import br.com.fiap.softmind.componentes.InterFont

@Composable
fun LanguageToggleButton() {
    var currentLanguage by remember { mutableStateOf("PT") }

    TextButton(
        onClick = {
            currentLanguage = if (currentLanguage == "PT") "EN" else "PT"
        }
    ) {
        Text(
            text = currentLanguage,
            fontFamily = InterFont,
            color = Color.White,
            fontSize = 20.sp
        )
    }
}