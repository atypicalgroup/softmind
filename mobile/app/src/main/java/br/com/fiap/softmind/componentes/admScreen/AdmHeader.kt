package br.com.fiap.softmind.componentes.admScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import br.com.fiap.softmind.R
import br.com.fiap.softmind.componentes.InterFont

@Composable
fun AdmHeader(modifier: Modifier){
    Column(){
        Text(
            text = stringResource(id = R.string.resumo_semanal),
            color = (Color(0xFF9E9E9E)),
            fontFamily = InterFont,
            fontSize = 28.sp

        )
        Text(
            text = stringResource(id = R.string.resumo_semanal1),
            fontWeight = FontWeight.Bold,
            fontFamily = InterFont,
            color = (Color(0xFF000000)),
            fontSize = 25.sp
        )
    }
}