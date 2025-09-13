package br.com.fiap.softmind.componentes.loginScreen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.softmind.R

@Composable
fun LoginTitle(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.texto_acesso),
        fontSize = 18.sp,
        color = Color(0xFF333333),
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 90.dp),
    )
}

@Preview(showBackground = true)
@Composable
fun LoginTitlePreview() {
    LoginTitle()
}