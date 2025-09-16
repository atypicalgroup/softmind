package br.com.fiap.softmind.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.fiap.softmind.R
import br.com.fiap.softmind.componentes.InterFont
import br.com.fiap.softmind.componentes.admScreen.AdmCalendar
import br.com.fiap.softmind.componentes.admScreen.AdmHeader
import br.com.fiap.softmind.componentes.admScreen.AdmTextHeader
import br.com.fiap.softmind.data.model.Engagement
import br.com.fiap.softmind.utils.generatePdfFromEngagement
import br.com.fiap.softmind.utils.openPdfFile
import br.com.fiap.softmind.viewmodel.EngagementViewModel

@Composable
fun AdminScreen(
    navController: NavController,
    viewModel: EngagementViewModel = viewModel()
) {

    val engajamento = viewModel.engagementSelected.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEEF0F6))
            .padding(top = 26.dp, start = 8.dp, end = 8.dp, bottom = 16.dp)
    ) {
        AdmHeader(modifier = Modifier)

        Spacer(modifier = Modifier.height(1.dp))

        AdmTextHeader(modifier = Modifier)

        Spacer(modifier = Modifier.height(2.dp))

        AdmCalendar(navController, viewModel)

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(180.dp), // altura fixa por causa da internacioalização
                colors = CardDefaults.cardColors(containerColor = Color(0xFFD0E9BC))
            ) {
                Column(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF76A253), shape = RoundedCornerShape(22.dp))
                            .padding(horizontal = 20.dp, vertical = 2.dp)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = stringResource(id = R.string.dados),
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontFamily = InterFont,
                            fontSize = 22.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "${engajamento?.engajamento_colaboradores?.toInt() ?: "--"}%",
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        fontSize = 50.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = InterFont,
                        color = Color(0xFF76A253)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(3.dp),
                        contentAlignment = Alignment.Center

                    ) {
                        Text(
                            text = stringResource(id = R.string.percentual),
                            color = Color(0xFF76A253),
                            fontSize = 15.sp,
                            fontFamily = InterFont,
                            modifier = Modifier
                                .padding(2.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(180.dp), // <- mesma altura fixa
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0E68C))
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF000000), shape = RoundedCornerShape(22.dp))
                            .padding(horizontal = 20.dp, vertical = 2.dp)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = stringResource(id = R.string.dados),
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontFamily = InterFont,
                            fontSize = 22.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = engajamento?.sentimento_destaque?.let { mapEmoji(it) } ?: "🙂",
                        fontSize = 50.sp,
                        fontFamily = InterFont,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(3.dp),
                        contentAlignment = Alignment.Center

                    ) {
                        Text(
                            text = stringResource(id = R.string.sentimento_destaque),
                            color = Color.Black,
                            fontSize = 15.sp,
                            fontFamily = InterFont,
                            modifier = Modifier
                                .padding(2.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFD0E9BC))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(6.dp),
                    verticalArrangement = Arrangement.Center
                ) {

                        Text(
                            text = stringResource(id = R.string.bem_estar) + " " +
                                    stringResource(id = R.string.bem_estar1) + " " +
                            "${engajamento?.bem_estar_emocional ?: "--"}%.",
                            color = Color.Black,
                            fontFamily = InterFont,
                            fontSize = 16.sp
                        )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = engajamento?.comentario ?: stringResource(id = R.string.pesquisa) + " " +
                                stringResource(id = R.string.positivo),
                        fontFamily = InterFont,
                        fontSize = 11.sp,
                        color = Color.Black,

                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    val context = LocalContext.current
                    Button(
                        onClick = {
                            engajamento?.let {
                                val file = generatePdfFromEngagement(context, it)
                                file?.let { openPdfFile(context, it) }
                            }
                        },
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF76A253),
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = stringResource(id = R.string.imprimir), fontFamily = InterFont)
                    }
                }

                Image(
                    painter = painterResource(id = R.drawable.womanadm),
                    contentDescription = stringResource(id = R.string.adm_medica),
                    modifier = Modifier
                        .size(170.dp)
                        .align(Alignment.Bottom)
                        .padding(start = 16.dp)
                )
            }
        }
    }
}

fun mapEmoji(fileName: String?): String {
    return when (fileName) {
        "emoji1.png" -> "😀"
        "emoji2.png" -> "😐"
        "emoji3.png" -> "😢"
        "emoji4.png" -> "😠"
        else -> "🙂"
    }
}


@Preview(showSystemUi = true, locale = "pt-rBR")
@Composable
private fun AdminScreenPreview() {
    val navController = rememberNavController()
    val fakeViewModel = object : EngagementViewModel() {
        init {
            _engagementSelected.value = Engagement(
                engajamento_colaboradores = 85.0,
                sentimento_destaque = "emoji1.png",
                bem_estar_emocional = 70.0,
                variacao_percentual = 5.0,
                tendencia = "crescimento",
                emoji_representativo = "emoji1.png",
                comentario = "Colaboradores se mostraram mais confiantes",
                periodo = "13/05/2025 a 19/05/2025"
            )
        }
    }
    AdminScreen(navController, viewModel = fakeViewModel)
}