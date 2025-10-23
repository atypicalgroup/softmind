package br.com.fiap.softmind.screens

import ForgotPasswordDialog
import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import br.com.fiap.softmind.R
import br.com.fiap.softmind.componentes.LoginDescription
import br.com.fiap.softmind.componentes.StartButton
import br.com.fiap.softmind.componentes.loginScreen.*
import br.com.fiap.softmind.data.remote.ApiClient
import br.com.fiap.softmind.data.remote.model.*
import kotlinx.coroutines.launch
import org.json.JSONObject

// Gerencia os estados dos diálogos
private enum class DialogState {
    NONE,
    FORGOT_PASSWORD,
    VALIDATE_CODE,
    CREATE_PASSWORD
}

@SuppressLint("StringFormatInvalid")
@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Estado do fluxo de redefinição
    var dialogState by remember { mutableStateOf(DialogState.NONE) }
    var resetEmail by remember { mutableStateOf("") }
    var resetToken by remember { mutableStateOf("") }

    // ==========================================================
    // 🔹 Controle central dos diálogos
    // ==========================================================
    when (dialogState) {

        // 1️⃣ Esqueci a senha
        DialogState.FORGOT_PASSWORD -> {
            ForgotPasswordDialog(
                onDismissRequest = { dialogState = DialogState.NONE },
                onConfirm = { emailParaReset ->
                    resetEmail = emailParaReset
                    coroutineScope.launch {
                        try {
                            val response = ApiClient.authService.forgotPassword(
                                ForgotPasswordRequest(emailParaReset)
                            )
                            if (response.isSuccessful) {
                                Toast.makeText(context, "Código enviado para $emailParaReset", Toast.LENGTH_LONG).show()
                                dialogState = DialogState.VALIDATE_CODE
                            } else {
                                Toast.makeText(context, "Erro ao enviar código.", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Log.e("FORGOT_PASSWORD", "Erro: ${e.message}")
                            Toast.makeText(context, "Falha de conexão: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            )
        }

        // 2️⃣ Validação do código recebido por e-mail
        DialogState.VALIDATE_CODE -> {
            ValidateCodeDialog(
                onDismissRequest = { dialogState = DialogState.NONE },
                onConfirm = { token ->
                    coroutineScope.launch {
                        try {
                            val response = ApiClient.authService.validateToken(
                                ValidateCodeRequest(email = resetEmail, token = token)
                            )
                            if (response.isSuccessful) {
                                Toast.makeText(context, "Código validado!", Toast.LENGTH_SHORT).show()
                                resetToken = token
                                dialogState = DialogState.CREATE_PASSWORD
                            } else {
                                Toast.makeText(context, "Código inválido ou expirado.", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Log.e("VALIDATE_CODE", "Erro: ${e.message}")
                            Toast.makeText(context, "Falha de conexão: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                onResendCode = {
                    coroutineScope.launch {
                        try {
                            val response = ApiClient.authService.forgotPassword(ForgotPasswordRequest(email = resetEmail))
                            if (response.isSuccessful) {
                                Toast.makeText(context, "Novo código enviado para $resetEmail!", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Erro ao reenviar código.", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Falha: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                email = resetEmail
            )
        }

        // 3️⃣ Criação de nova senha — primeiro login ou redefinição comum
        DialogState.CREATE_PASSWORD -> {
            if (ApiClient.loggedUserId != null && resetToken.isEmpty()) {
                // Primeiro login
                CreateNewPasswordDialog(
                    onDismissRequest = { dialogState = DialogState.NONE },
                    onConfirmNormal = {},
                    onConfirmFirstLogin = { newPassword ->
                        coroutineScope.launch {
                            try {
                                val response = ApiClient.authService.changePasswordFirstAccess(
                                    FirstLoginResetRequest(
                                        userId = ApiClient.loggedUserId ?: "",
                                        newPassword = newPassword
                                    )
                                )

                                if (response.isSuccessful) {
                                    Toast.makeText(context, "Senha criada com sucesso! Faça login novamente.", Toast.LENGTH_LONG).show()
                                    dialogState = DialogState.NONE
                                    email = ""
                                    password = ""
                                } else {
                                    Toast.makeText(context, "Erro ao criar nova senha.", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Falha de conexão: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    isFirstLogin = true
                )
            } else {
                // Redefinição de senha comum
                CreateNewPasswordDialog(
                    onDismissRequest = { dialogState = DialogState.NONE },
                    onConfirmNormal = { newPassword ->
                        coroutineScope.launch {
                            try {
                                val response = ApiClient.authService.changePassword(
                                    ResetPasswordRequest(
                                        email = resetEmail,
                                        token = resetToken,
                                        newPassword = newPassword
                                    )
                                )
                                if (response.isSuccessful) {
                                    Toast.makeText(context, "Senha redefinida com sucesso!", Toast.LENGTH_LONG).show()
                                    dialogState = DialogState.NONE
                                } else {
                                    Toast.makeText(context, "Erro ao redefinir senha.", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Falha de conexão: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    isFirstLogin = false
                )
            }
        }

        DialogState.NONE -> Unit // Nenhum diálogo ativo
    }

    // ==========================================================
    // 🔹 Layout principal da tela de login
    // ==========================================================
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .background(Color(0xFFEEF0F6)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Spacer(modifier = Modifier.weight(0.2f))
        LoginLogo()
        Spacer(modifier = Modifier.height(62.dp))
        LoginTitle()
        Spacer(modifier = Modifier.height(24.dp))

        EmailInputField(email = email, onValueChange = { email = it })
        Spacer(modifier = Modifier.height(16.dp))
        PasswordInputField(password = password, onValueChange = { password = it })
        Spacer(modifier = Modifier.height(8.dp))

        ClickableTextLink(
            text = stringResource(id = R.string.esqueceu_senha),
            onClick = { dialogState = DialogState.FORGOT_PASSWORD }
        )

        Spacer(modifier = Modifier.height(24.dp))
        LoginDescription()
        Spacer(modifier = Modifier.height(24.dp))

        // ==========================================================
        // 🔹 Botão de Login
        // ==========================================================
        StartButton(onClick = {
            if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(context, "Digite um e-mail válido.", Toast.LENGTH_SHORT).show()
                return@StartButton
            }

            coroutineScope.launch {
                try {
                    val response = ApiClient.authService.login(LoginRequest(username = email, password = password))
                    val json = response.body()?.string() ?: response.errorBody()?.string().orEmpty()
                    Log.d("LOGIN_RESPONSE", "HTTP ${response.code()} → $json")

                    val jsonObject = JSONObject(json)
                    when {
                        // 🔸 Primeiro login
                        response.code() == 403 && jsonObject.has("message") && jsonObject.has("userId") -> {
                            val pending = LoginPendingChangeResponse(
                                userId = jsonObject.getString("userId"),
                                username = jsonObject.getString("username"),
                                message = jsonObject.getString("message")
                            )
                            ApiClient.loggedUserId = pending.userId
                            resetEmail = pending.username
                            Toast.makeText(context, pending.message, Toast.LENGTH_LONG).show()
                            dialogState = DialogState.CREATE_PASSWORD
                        }

                        // 🔸 Login comum com JWT
                        jsonObject.has("token") -> {
                            val normal = LoginResponse(
                                token = jsonObject.getString("token"),
                                username = jsonObject.getString("username"),
                                name = jsonObject.getString("name")
                            )
                            ApiClient.authToken = normal.token
                            ApiClient.loggedUserName = normal.name
                            ApiClient.loggedUsername = normal.username

                            Toast.makeText(context, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show()
                            if (email.contains("admin")) navController.navigate("AdminScreen")
                            else navController.navigate("EmojiScreen")
                        }

                        // 🔸 Outros erros
                        else -> {
                            Toast.makeText(context, "Usuário ou senha incorretos.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    Log.e("LOGIN", "Erro: ${e.message}")
                    Toast.makeText(context, "Falha na conexão: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
        Spacer(modifier = Modifier.weight(0.3f))
    }
}

@Preview(showBackground = true, locale = "pt-BR")
@Composable
fun LoginScreenPreview() {
    LoginScreen(navController = NavHostController(LocalContext.current))
}
