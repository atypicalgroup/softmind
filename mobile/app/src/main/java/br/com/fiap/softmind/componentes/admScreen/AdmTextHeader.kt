package br.com.fiap.softmind.componentes.admScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.fiap.softmind.R
import br.com.fiap.softmind.componentes.InterFont

@Composable
fun AdmTextHeader(modifier: Modifier){
    Column(modifier = Modifier){
        Text(
            text = stringResource(id = R.string.texto_resumo),
            fontFamily = InterFont,
            style = MaterialTheme.typography.bodyMedium,
            color = (Color(0xFF9E9E9E))
        )
    }
}

@Preview(showSystemUi = true, locale = "pt-rBR")
@Composable
fun AdmTextHeaderPreview(){
    AdmTextHeader(modifier = Modifier)
}