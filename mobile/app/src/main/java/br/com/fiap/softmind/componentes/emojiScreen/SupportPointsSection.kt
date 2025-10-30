package br.com.fiap.softmind.componentes.emojiScreen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.fiap.softmind.R
import br.com.fiap.softmind.data.remote.model.SupportPoint
import br.com.fiap.softmind.viewmodel.SupportViewModel

@Composable
fun SupportPointsSection(
    modifier: Modifier = Modifier,
    viewModel: SupportViewModel = viewModel()
) {
    var showDialog by remember { mutableStateOf(false) }
    val supportPoints by viewModel.supportPoints.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // 🔹 Carrega os pontos assim que o componente for exibido
    LaunchedEffect(Unit) {
        viewModel.loadSupportPoints()
    }

    // 🔹 Botão principal que abre o modal
    SupportPointsButton(
        nome = stringResource(R.string.pontos_de_apoio),
        onClick = { showDialog = true },
        modifier = modifier
    )

    // 🔹 Modal com a lista de pontos
    if (showDialog) {
        SupportPointsDialog(
            onDismissRequest = { showDialog = false },
            isLoading = isLoading,
            supportPoints = supportPoints
        )
    }
}

@Composable
private fun SupportPointsDialog(
    onDismissRequest: () -> Unit,
    isLoading: Boolean,
    supportPoints: List<SupportPoint>
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismissRequest,
        shape = RoundedCornerShape(16.dp),
        title = {
            Text(
                text = stringResource(R.string.titulo_pontos_de_apoio),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            if (isLoading) {
                Text("Carregando pontos de apoio...", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    if (supportPoints.isEmpty()) {
                        Text(
                            text = "Nenhum ponto de apoio encontrado.",
                            textAlign = TextAlign.Center
                        )
                    } else {
                        supportPoints.forEach { point ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .background(Color(0xFFF5F5F5), RoundedCornerShape(12.dp))
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = point.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                                Text(
                                    text = point.description,
                                    fontSize = 14.sp,
                                    color = Color.DarkGray
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                point.contactNumber.forEach { phone ->
                                    Text(
                                        text = phone,
                                        color = Color(0xFF00BFA5),
                                        textDecoration = TextDecoration.Underline,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.clickable {
                                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                                data = Uri.parse("tel:$phone")
                                            }
                                            context.startActivity(intent)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                DialogBackButton(onClick = onDismissRequest)
            }
        }
    )
}

@Composable
fun SupportPointsButton(
    nome: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth(0.8f)
            .height(52.dp)
            .clip(RoundedCornerShape(26.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color(0xFF98FB98), Color(0xFF62BEC3))
                )
            ),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
    ) {
        Text(
            text = nome,
            color = Color.Black,
            fontSize = 18.sp,
            letterSpacing = 0.3.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun DialogBackButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(0.5f)
            .height(48.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color(0xFF98FB98), Color(0xFF62BEC3))
                ),
                shape = RoundedCornerShape(24.dp)
            ),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
    ) {
        Text(
            text = stringResource(R.string.voltar),
            color = Color.Black,
            fontSize = 18.sp,
            letterSpacing = 0.3.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SupportPointsSectionPreview() {
    SupportPointsSection()
}
