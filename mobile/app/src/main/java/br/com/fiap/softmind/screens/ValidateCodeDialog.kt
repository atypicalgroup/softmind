package br.com.fiap.softmind.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults.outlinedTextFieldColors
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.softmind.R

// Constante para o número de dígitos
private const val CODE_LENGTH = 6

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ValidateCodeDialog(
    onDismissRequest: () -> Unit,
    onConfirm: (code: String) -> Unit,
    onResendCode: () -> Unit,
    email: String, // Para contextualizar o envio do código (opcional)
    modifier: Modifier = Modifier
) {
    // Lista de estados para cada dígito (6 dígitos)
    val codeFields = remember {
        mutableStateListOf(
            *(1..CODE_LENGTH).map { mutableStateOf(TextFieldValue("")) }.toTypedArray()
        )
    }

    // Array de FocusRequesters para controlar o foco entre os campos
    val focusRequesters = remember {
        List(CODE_LENGTH) { FocusRequester() }
    }

    // Variável de estado para o código completo
    val fullCode = codeFields.joinToString("") { it.value.text }

    // Habilita o botão de validação se o código completo tiver o tamanho correto
    val isConfirmButtonEnabled = fullCode.length == CODE_LENGTH

    // Requisitar foco no primeiro campo ao iniciar
    LaunchedEffect(Unit) {
        focusRequesters.first().requestFocus()
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = stringResource(R.string.validar_codigo),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Digite o código de $CODE_LENGTH dígitos enviado para o seu e-mail.", // Usar stringResource se disponível
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))

                // Componente para a entrada dos dígitos
                CodeInputFields(
                    codeFields = codeFields,
                    focusRequesters = focusRequesters,
                    length = CODE_LENGTH
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { onConfirm(fullCode) },
                    enabled = isConfirmButtonEnabled,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00BFA5) // Cor de destaque
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(
                        text = stringResource(R.string.validar_codigo),
                        color = Color.White,
                        fontSize = 18.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Link para reenviar o código
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.nao_recebeu_codigo),
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    TextButton(onClick = onResendCode) {
                        Text(
                            text = stringResource(R.string.reenviar),
                            color = Color(0xFF00BFA5)
                        )
                    }
                }
            }
        },
        // Remove os botões padrão do AlertDialog para usar o layout customizado acima
        confirmButton = {},
        dismissButton = {}
    )
}

/**
 * Componente interno que renderiza os campos de entrada de código.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CodeInputFields(
    codeFields: List<androidx.compose.runtime.MutableState<TextFieldValue>>,
    focusRequesters: List<FocusRequester>,
    length: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // Itera sobre o número de dígitos para criar os campos
        codeFields.forEachIndexed { index, codeFieldState ->
            val isFocused = remember { mutableStateOf(false) }

            OutlinedTextField(
                value = codeFieldState.value,
                onValueChange = { newValue ->
                    // Aceita apenas o primeiro dígito
                    val newDigit = newValue.text.take(1)

                    // Se o campo não estiver vazio e o novo valor for diferente, avança para o próximo
                    if (newDigit.isNotEmpty()) {
                        codeFieldState.value = TextFieldValue(
                            text = newDigit,
                            selection = TextRange(newDigit.length)
                        )
                        // Avança o foco, se não for o último campo
                        if (index < length - 1) {
                            focusRequesters[index + 1].requestFocus()
                        }
                    } else if (newValue.text.isEmpty() && codeFieldState.value.text.isEmpty()){
                        // Se o valor for limpo, mantém o campo limpo
                        codeFieldState.value = TextFieldValue("")
                    }
                },
                // Configurações do teclado: numérico, ação "próximo" ou "feito" no último
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = if (index < length - 1) ImeAction.Next else ImeAction.Done
                ),
                singleLine = true,
                modifier = Modifier
                    .weight(1f) // Distribui o peso igualmente
                    .padding(horizontal = 4.dp)
                    .size(56.dp) // Tamanho fixo para cada quadrado
                    .focusRequester(focusRequesters[index])
                    .onFocusChanged { focusState ->
                        isFocused.value = focusState.isFocused
                    }
                    .onKeyEvent {
                        // Lógica para Backspace
                        if (it.key == Key.Backspace && codeFieldState.value.text.isEmpty() && index > 0) {
                            // Se o campo estiver vazio, move o foco para o campo anterior
                            focusRequesters[index - 1].requestFocus()
                            // Limpa o campo anterior
                            codeFields[index - 1].value = TextFieldValue("")
                            true
                        } else {
                            false
                        }
                    },
                // Estilo para centralizar o texto e aumentar a fonte
                textStyle = LocalTextStyle.current.copy(
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                ),
                // Personalização do campo OutlinedTextField (similar ao ForgotPasswordDialog)
                colors = TextFieldDefaults.run {
                    outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF00BFA5), // Cor de destaque
                        unfocusedBorderColor = Color(0xFFD9D9D9),
                        cursorColor = Color(0xFF00BFA5),
//                        containerColor = Color.Transparent, // Fundo transparente
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )
                },
                shape = RoundedCornerShape(8.dp), // Borda quadrada/arredondada (similar à imagem)
                // Remove o label
                label = null
            )
        }
    }
}


@Preview(showBackground = true, locale = "pt-rBR")
@Composable
fun ValidateCodeDialogPreview() {
    // Simulação da tela de validação em Preview
    ValidateCodeDialog(
        onDismissRequest = {},
        onConfirm = {},
        onResendCode = {},
        email = "usuario@exemplo.com"
    )
}