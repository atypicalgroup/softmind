package br.com.fiap.softmind.componentes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import br.com.fiap.softmind.R

@Composable
fun HeaderEnd() {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = stringResource(id = R.string.parabens),
            color = Color(0xFF9E9E9E),
            fontFamily = InterFont,
            fontSize = 28.sp
        )
        Text(
            text = stringResource(id = R.string.parabens1),
            color = Color.Black,
            fontFamily = InterFont,
            fontSize = 28.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HeaderEndPreview() {
    HeaderEnd()
}