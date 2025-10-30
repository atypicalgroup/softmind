package br.com.fiap.softmind.screens.administrative

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import br.com.fiap.softmind.componentes.admScreen.*
import br.com.fiap.softmind.data.model.AdminReport
import br.com.fiap.softmind.utils.generatePdfFromReport
import br.com.fiap.softmind.utils.openPdfFile
import br.com.fiap.softmind.viewmodel.EngagementViewModel
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
@Composable
fun AdminScreen(
    navController: NavController,
    viewModel: EngagementViewModel = viewModel()
) {
    val report by viewModel.adminReport.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadAdminReport()
    }

    when {
        isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF76A253))
            }
        }
        errorMessage != null -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "Erro ao carregar: $errorMessage",
                    color = Color.Red,
                    fontFamily = InterFont,
                    fontSize = 16.sp
                )
            }
        }
        report != null -> {
            AdminReportContent(navController, report!!)
        }
    }
}

@Composable
private fun AdminReportContent(navController: NavController, report: AdminReport) {

    val context = LocalContext.current
    val engajamento = report.weekSummary?.overallEngagement?.toInt() ?: 0
    val sentimento = report.moodSummary?.mostCommonMood ?: "emoji1.png"
    val comentario = report.alerts?.firstOrNull() ?: "Nenhum alerta para esta semana"
    val bemEstar = report.currentHealthyPercentage ?: 0.0

    val scrollState = rememberScrollState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEEF0F6))
            .verticalScroll(scrollState)
            .padding(top = 26.dp, start = 8.dp, end = 8.dp, bottom = 16.dp)
    ) {
        // Cabeçalho e calendário
        AdmHeader(modifier = Modifier)
        Spacer(modifier = Modifier.height(4.dp))
        AdmTextHeader(modifier = Modifier)
        Spacer(modifier = Modifier.height(8.dp))
        AdmCalendar(navController)

        Spacer(modifier = Modifier.height(16.dp))

        // Cards de dados principais
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Card de Engajamento
            InfoCard(
                title = stringResource(id = R.string.dados),
                value = "$engajamento%",
                subtitle = stringResource(id = R.string.percentual),
                backgroundColor = Color(0xFFD0E9BC),
                accentColor = Color(0xFF76A253)
            )

            // Card de Sentimento
            InfoCard(
                title = stringResource(id = R.string.dados),
                value = mapEmoji(sentimento),
                subtitle = stringResource(id = R.string.sentimento_destaque),
                backgroundColor = Color(0xFFF0E68C),
                accentColor = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Card detalhado
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFD0E9BC))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
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
                                "$bemEstar%.",
                        color = Color.Black,
                        fontFamily = InterFont,
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = comentario,
                        fontFamily = InterFont,
                        fontSize = 11.sp,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            val file = generatePdfFromReport(context, report)
                            file?.let { openPdfFile(context, it) }
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

        Spacer(modifier = Modifier.height(16.dp))

        LogoutButton(
            onClick = { navController.navigate("LoginScreen") },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
private fun InfoCard(
    title: String,
    value: String,
    subtitle: String,
    backgroundColor: Color,
    accentColor: Color
) {
    Card(
        modifier = Modifier
            .height(180.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Box(
                modifier = Modifier
                    .background(accentColor, shape = RoundedCornerShape(22.dp))
                    .padding(horizontal = 20.dp, vertical = 4.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontFamily = InterFont,
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = value,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = InterFont,
                color = accentColor
            )

            Text(
                text = subtitle,
                color = accentColor,
                fontSize = 15.sp,
                fontFamily = InterFont,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center
            )
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
    AdminReportContent(
        navController,
        AdminReport(
            weekSummary = null,
            currentHealthyPercentage = 70.0,
            alerts = listOf("Colaboradores se mostraram mais confiantes")
        )
    )
}
