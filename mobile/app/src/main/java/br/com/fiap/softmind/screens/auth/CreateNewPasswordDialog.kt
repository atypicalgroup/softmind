package br.com.fiap.softmind.screens.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults.outlinedTextFieldColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.fiap.softmind.R

// 🎨 Paleta padrão
private val DestaqueColor = Color(0xFF00BFA5)
private val ErrorColor = Color(0xFFFF5252)
private val ContainerColor = Color(0xFFF0F0F0)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateNewPasswordDialog(
    onDismissRequest: () -> Unit,
    onConfirmNormal: (newPassword: String) -> Unit,                   // 🔹 /auth/change-password
    onConfirmFirstLogin: ((newPassword: String) -> Unit)? = null,     // 🔹 /auth/change-password-first-access
    isFirstLogin: Boolean = false,                                   // 🔹 Define o contexto do fluxo
    modifier: Modifier = Modifier
) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var hasInteracted by remember { mutableStateOf(false) }

    var isNewPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }

    // ✅ Regras de validação
    val isPasswordValid = newPassword.length >= 6
    val passwordsMatch = newPassword == confirmPassword
    val isConfirmButtonEnabled = isPasswordValid && passwordsMatch

    val showLengthError = hasInteracted && !isPasswordValid && newPassword.isNotEmpty()
    val showMatchError = hasInteracted && newPassword.isNotEmpty() && confirmPassword.isNotEmpty() && !passwordsMatch

    // Foco automático no primeiro campo
    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = if (isFirstLogin) "Crie sua senha definitiva" else stringResource(R.string.criar_nova_senha),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (isFirstLogin)
                        "Por segurança, defina uma nova senha antes de continuar."
                    else
                        stringResource(R.string.confirmar_nova_senha),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 🔐 Campo Nova Senha
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newValue ->
                        newPassword = newValue
                        if (!hasInteracted && newValue.isNotEmpty()) hasInteracted = true
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null, tint = Color(0xFF333333)) },
                    trailingIcon = {
                        val image = if (isNewPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { isNewPasswordVisible = !isNewPasswordVisible }) {
                            Icon(imageVector = image, contentDescription = "Alternar visibilidade de senha")
                        }
                    },
                    visualTransformation = if (isNewPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    label = { Text(stringResource(R.string.nova_senha)) },
                    isError = showLengthError,
                    shape = RoundedCornerShape(36.dp),
                    colors = TextFieldDefaults.run {
                        outlinedTextFieldColors(
                            focusedBorderColor = DestaqueColor,
                            unfocusedBorderColor = Color(0xFFD9D9D9),
                            cursorColor = DestaqueColor,
                            unfocusedContainerColor = ContainerColor,
                            errorBorderColor = ErrorColor,
                            errorCursorColor = ErrorColor
                        )
                    },
                    modifier = modifier.fillMaxWidth().focusRequester(focusRequester)
                )

                if (showLengthError) {
                    Text(
                        text = stringResource(R.string.senha_invalida),
                        color = ErrorColor,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 4.dp, end = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 🔐 Campo Confirmar Senha
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { newValue ->
                        confirmPassword = newValue
                        if (!hasInteracted && newValue.isNotEmpty()) hasInteracted = true
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null, tint = Color(0xFF333333)) },
                    trailingIcon = {
                        val image = if (isConfirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { isConfirmPasswordVisible = !isConfirmPasswordVisible }) {
                            Icon(imageVector = image, contentDescription = "Alternar visibilidade de senha")
                        }
                    },
                    visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    label = { Text(stringResource(R.string.confirmar_senha)) },
                    isError = showMatchError,
                    shape = RoundedCornerShape(36.dp),
                    colors = TextFieldDefaults.run {
                        outlinedTextFieldColors(
                            focusedBorderColor = DestaqueColor,
                            unfocusedBorderColor = Color(0xFFD9D9D9),
                            cursorColor = DestaqueColor,
                            unfocusedContainerColor = ContainerColor,
                            errorBorderColor = ErrorColor,
                            errorCursorColor = ErrorColor
                        )
                    },
                    modifier = modifier.fillMaxWidth()
                )

                if (showMatchError) {
                    Text(
                        text = stringResource(R.string.senha_diferente),
                        color = ErrorColor,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 4.dp, end = 16.dp)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (isFirstLogin && onConfirmFirstLogin != null)
                        onConfirmFirstLogin(newPassword)
                    else
                        onConfirmNormal(newPassword)
                },
                enabled = isConfirmButtonEnabled
            ) {
                Text(text = if (isFirstLogin) "Definir senha" else "Redefinir senha")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(R.string.cancelar))
            }
        }
    )
}

@Preview(showBackground = true, locale = "pt-rBR")
@Composable
fun CreateNewPasswordDialogPreview() {
    CreateNewPasswordDialog(
        onDismissRequest = {},
        onConfirmNormal = {},
        isFirstLogin = false
    )
}
