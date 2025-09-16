package br.com.fiap.softmind.componentes.questions

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BoxTextQuestion(
    current: Int,
    total: Int
){
    Text(
        text = "Pergunta $current",
        fontSize = 15.sp,
        color = Color.Gray,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun BoxTextQuestionPreview() {
    BoxTextQuestion(current = 1, total = 10)
}

