package br.com.fiap.softmind.componentes.presentationScreen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import br.com.fiap.softmind.R
import br.com.fiap.softmind.componentes.BalooFont

@Composable
fun NomeApp() {
    Text(
        text = stringResource(id = R.string.app_name),
        fontFamily = BalooFont,
        fontWeight = FontWeight.Normal,
        color = Color.White,
        fontSize = 30.sp,
    )
}