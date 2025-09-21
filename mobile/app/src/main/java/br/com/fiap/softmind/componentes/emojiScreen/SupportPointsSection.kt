package br.com.fiap.softmind.componentes.emojiScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.softmind.R
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration

@Composable
fun SupportPointsSection(
    //Parametros que serão carregados do backend
    horarioFuncionamento: String,
    nomeResponsavel: String,
    telefoneResponsavel: String,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }

    // Botão que abre o diálogo
    SupportPointsButton(
        onClick = { showDialog = true },
        modifier = modifier
    )

    if (showDialog) {
        //Passando os dados recebidos para o dialogo
        SupportPointsDialog(
            onDismissRequest = { showDialog = false },
            horarioFuncionamento = horarioFuncionamento,
            nomeResponsavel = nomeResponsavel,
            telefoneResponsavel = telefoneResponsavel
        )
    }
}

@Composable
private fun SupportPointsDialog(
    onDismissRequest: () -> Unit,
    horarioFuncionamento: String,
    nomeResponsavel: String,
    telefoneResponsavel: String
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
        // Conteúdo do Diálogo
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = horarioFuncionamento,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))

                Column(horizontalAlignment = Alignment.Start) {
                    Row {
                        Text(
                            text = stringResource(R.string.nome_responsavel_label),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = nomeResponsavel
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row {
                        Text(
                            text = stringResource(R.string.telefone_responsavel_label),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = telefoneResponsavel,
                            color = Color(0xFF00BFA5),
                            textDecoration = TextDecoration.Underline,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable {
                                val intent = Intent(Intent.ACTION_DIAL).apply {
                                    data = Uri.parse("tel:$telefoneResponsavel")
                                }
                                context.startActivity(intent)
                            }
                        )
                    }
                }
            }
        },
        // Botão de ação ("Voltar")
        confirmButton = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                DialogBackButton(onClick = onDismissRequest)
            }
        }
    )
}

@Composable
private fun SupportPointsButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth(0.6f)
            .height(48.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color(0xFF98FB98), Color(0xFF62BEC3))
                ),
                shape = RoundedCornerShape(24.dp)
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(
                text = stringResource(R.string.pontos_de_apoio),
                color = Color.Black,
                fontSize = 18.sp,
                letterSpacing = 0.3.sp
            )
        }
    }
}

@Composable
private fun DialogBackButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth(0.5f)
            .height(48.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color(0xFF98FB98), Color(0xFF62BEC3))
                ),
                shape = RoundedCornerShape(24.dp)
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(
                text = stringResource(R.string.voltar),
                color = Color.Black,
                fontSize = 18.sp,
                letterSpacing = 0.3.sp
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun SupportPointsSectionPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        SupportPointsSection(
            horarioFuncionamento = "Segunda a Sexta, das 08:00 às 18:00.",
            nomeResponsavel = "Dr. Carlos Andrade",
            telefoneResponsavel = "(11) 91234-5678"
        )
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun SupportPointsDialogPreview() {
    SupportPointsDialog(
        onDismissRequest = {},
        horarioFuncionamento = "Segunda a Sexta, das 08:00 às 18:00.",
        nomeResponsavel = "Dr. Carlos Andrade",
        telefoneResponsavel = "(11) 91234-5678"
    )
}