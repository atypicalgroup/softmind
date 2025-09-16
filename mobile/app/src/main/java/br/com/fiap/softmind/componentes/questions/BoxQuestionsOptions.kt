package br.com.fiap.softmind.componentes.questions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.softmind.R

@Composable
fun BoxQuestions(
    question: String,
    options: List<String>,
    onSelected: (String) -> Unit,
    onBackQuestions: () -> Unit,
    currentQuestion: Int,
    totalQuestions: Int
) {
    var selectedOption by remember(question) { mutableStateOf<String?>(null) }
    val interFont = FontFamily(
        Font(R.font.inter_regular, FontWeight.Normal)
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF8F8F8), shape = RoundedCornerShape(24.dp))
            .padding(horizontal = 8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .background(Color(0xFFD9D9D9), shape = RoundedCornerShape(60))
                    .padding(horizontal = 36.dp, vertical = 4.dp)
            )
            {
                Text(
                    text = stringResource(id = R.string.pergunta_variavel)
                            + " " + "${currentQuestion + 1}",
                    color = Color(0xFF727272),
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
                    .height(150.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = question,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    fontFamily = interFont
                )
            }

            Spacer(modifier = Modifier.height(50.dp))

            options.forEach { option ->
                OutlinedButton(
                    onClick = {
                        selectedOption = option
                        onSelected(option)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 0.dp, horizontal = 30.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (selectedOption == option) Color(0xFFD0E9BC) else Color(
                            0xFFF0E68C
                        ),
                        contentColor = if (selectedOption == option) Color(0xFF44543A) else Color.Black
                    ),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = option,
                        textAlign = TextAlign.Center,
                        fontFamily = interFont
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (currentQuestion > 0){
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    OutlinedButton(
                        onClick = {
                            onBackQuestions()
                        },
                        modifier = Modifier.fillMaxWidth(0.5f)
                            .height(45.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color(0xFFD9D9D9),
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(stringResource(id = R.string.voltar))
                    }
                }
            }
        }
    }
}

