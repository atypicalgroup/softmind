@file:Suppress("DEPRECATION")

package br.com.fiap.softmind.componentes.loginScreen

import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ClickableTextLink (
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    style: TextStyle = TextStyle.Default.copy(
        color = Color(0xFF00BFA5),
        textDecoration = TextDecoration.Underline
    )
){
    ClickableText(
        text = AnnotatedString(text),
        onClick = { onClick() },
        style = style,
        modifier = modifier
    )
}
