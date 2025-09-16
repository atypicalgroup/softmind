package br.com.fiap.softmind.componentes.presentationScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.softmind.R
import br.com.fiap.softmind.componentes.InterFont // Certifique-se de que InterFont está definido corretamente

@Composable
fun ConteudoTela(modifier: Modifier){
    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            stringResource(id = R.string.chamada),
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(end = 65.dp),
            color = Color.White,
            fontSize = 39.sp,
            fontFamily = InterFont,
            lineHeight = 34.sp,
            letterSpacing = 0.1.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ConteudoTelaPreview(){
    ConteudoTela(modifier = Modifier.fillMaxSize())
}