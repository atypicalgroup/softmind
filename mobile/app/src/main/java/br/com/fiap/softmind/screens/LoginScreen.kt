package br.com.fiap.softmind.screens

import ForgotPasswordDialog
import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import br.com.fiap.softmind.componentes.loginScreen.ClickableTextLink
import br.com.fiap.softmind.componentes.loginScreen.EmailInputField
import br.com.fiap.softmind.componentes.loginScreen.LoginLogo
import br.com.fiap.softmind.componentes.loginScreen.LoginTitle
import br.com.fiap.softmind.componentes.loginScreen.PasswordInputField
import br.com.fiap.softmind.data.remote.ApiClient
import br.com.fiap.softmind.data.remote.model.ForgotPasswordRequest
import br.com.fiap.softmind.data.remote.model.LoginRequest
import br.com.fiap.softmind.data.remote.model.ResetPasswordRequest
import br.com.fiap.softmind.data.remote.model.ValidateCodeRequest
import br.com.fiap.softmind.utils.JwtUtils
import kotlinx.coroutines.launch

// 1. ADICIONADO: Enum para gerenciar os estados de diálogo
private enum class DialogState {
    NONE, // Nenhum diálogo
    FORGOT_PASSWORD, // Diálogo de e-mail (seu ForgotPasswordDialog)
    VALIDATE_CODE, // Diálogo de código (seu ValidateCodeDialog)
    CREATE_PASSWORD // Diálogo de criação de senha (seu CreatePasswordDialog)
}

@SuppressLint("StringFormatInvalid")
@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // ADICIONADO RECENTEMENTE
    // ALTERADO: Usamos um único estado para rastrear qual diálogo está aberto
    var dialogState by remember { mutableStateOf(DialogState.NONE) }
    // ADICIONADO: Estado para guardar o e-mail que recebeu o código
    var resetEmail by remember { mutableStateOf("") }
    var resetToken by remember { mutableStateOf("") }

    // 1. ADICIONADO: Estado para controlar a visibilidade do pop-up
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // ALTERADO: Bloco centralizado para gerenciar a exibição dos diálogos
    when (dialogState) {
        DialogState.FORGOT_PASSWORD -> {
            ForgotPasswordDialog(
                onDismissRequest = {
                    dialogState = DialogState.NONE
                },
                onConfirm = { emailParaReset ->
                    resetEmail = emailParaReset

                    val logMessage = context.getString(R.string.msg_redefinicao_senha,
                        emailParaReset)
                    Log.d("RESET_SENHA", logMessage)

                    coroutineScope.launch {
                        try {
                            val response = ApiClient.authService.forgotPassword(
                                ForgotPasswordRequest(emailParaReset)
                            )

                            if (response.isSuccessful) {
                                Toast.makeText(
                                    context,
                                    "Código de redefinição enviado para $emailParaReset",
                                    Toast.LENGTH_LONG
                                ).show()
                                dialogState = DialogState.VALIDATE_CODE
                            } else {
                                Toast.makeText(
                                    context,
                                    "Erro ao enviar código. Verifique o e-mail informado.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        } catch (e: Exception) {
                            Log.e("FORGOT_PASSWORD", "Erro: ${e.message}")
                            Toast.makeText(context, "Falha de conexão: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    dialogState = DialogState.VALIDATE_CODE
                }
            )
        }

        DialogState.VALIDATE_CODE -> {
            ValidateCodeDialog(
                onDismissRequest = {
                    dialogState = DialogState.NONE // Fecha o diálogo
                },
                onConfirm = { token ->
                    coroutineScope.launch {
                        try {
                            val response = ApiClient.authService.validateToken(
                                ValidateCodeRequest(
                                    email = resetEmail,
                                    token = token
                                )
                            )

                            if (response.isSuccessful) {
                                Toast.makeText(context, "Código validado com sucesso!", Toast.LENGTH_SHORT).show()
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
                            val response = ApiClient.authService.forgotPassword(
                                ForgotPasswordRequest(email = resetEmail)
                            )

                            if (response.isSuccessful) {
                                Toast.makeText(
                                    context,
                                    "Novo código enviado para $resetEmail!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Erro ao reenviar o código. Tente novamente.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        } catch (e: Exception) {
                            Log.e("RESEND_CODE", "Erro: ${e.message}")
                            Toast.makeText(context, "Falha de conexão: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    Toast.makeText(context, "Código reenviado para $resetEmail.", Toast.LENGTH_SHORT).show()
                },
                email = resetEmail // Passa o email para contexto visual (se aplicável)
            )
        }

        // ADIÇÃO: O novo diálogo de criação de senha
        DialogState.CREATE_PASSWORD -> {
            CreateNewPasswordDialog(
                onDismissRequest = {
                    dialogState = DialogState.NONE
                },
                onConfirm = { newPassword ->
                    coroutineScope.launch {
                        try {
                            val response = ApiClient.authService.resetPassword(
                                ResetPasswordRequest(
                                    email = resetEmail,
                                    token = resetToken,
                                    newPassword = newPassword
                                )
                            )

                            if (response.isSuccessful) {
                                Toast.makeText(
                                    context,
                                    "Senha redefinida com sucesso para $resetEmail!",
                                    Toast.LENGTH_LONG
                                ).show()
                                dialogState = DialogState.NONE
                            } else {
                                Toast.makeText(
                                    context,
                                    "Erro ao redefinir senha. Tente novamente.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        } catch (e: Exception) {
                            Log.e("RESET_PASSWORD", "Erro: ${e.message}")
                            Toast.makeText(context, "Falha de conexão: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    // Fim do fluxo de reset: volta para o LoginScreen principal (DialogState.NONE)
                    dialogState = DialogState.NONE
                }
            )
        }

        DialogState.NONE -> {
            // Não renderiza nada, continua no fluxo normal da LoginScreen
        }
    }


    //CODIGO ANTIGO
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
            onClick = {

                //CODIGO NOVO
                dialogState = DialogState.FORGOT_PASSWORD

                // CODIGO ANTIGO
//                showDialog = true

            })

        Spacer(modifier = Modifier.height(24.dp))

        LoginDescription()

        Spacer(modifier = Modifier.height(24.dp))

        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()
        StartButton(onClick = {
            if (email.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                coroutineScope.launch {
                    try {
                        // Cria o objeto de request
                        val request = LoginRequest(username = email, password = password)

                        // Faz a chamada ao backend (suspend fun, sem enqueue)
                        val response = ApiClient.authService.login(request)

                        if (response.isSuccessful) {
                            val loginResponse = response.body()
                            val token = loginResponse?.token

                            if (token != null) {
                                Log.d("LOGIN", "Token recebido: $token")
                                Toast.makeText(context, "Login realizado com sucesso", Toast.LENGTH_SHORT).show()

                                val payload = JwtUtils.decodePayload(token)
                                ApiClient.authToken = token
                                ApiClient.loggedUserName = payload?.optString("name")
                                ApiClient.loggedUsername = payload?.optString("username")

                                Log.d("LOGIN", "Nome: ${ApiClient.loggedUserName}")
                                Log.d("LOGIN", "Username: ${ApiClient.loggedUsername}")

                                if (email.contains("admin")) {
                                    navController.navigate("AdminScreen")
                                } else {
                                    navController.navigate("EmojiScreen")
                                }
                            }

                        } else {
                            Toast.makeText(context, "Usuário ou senha incorretos", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(context, "Falha na conexão: ${e.message}", Toast.LENGTH_SHORT).show()
                        Log.e("LOGIN", "Erro ao fazer login", e)
                    }
                }
            } else {
                Toast.makeText(context, "Digite um e-mail válido", Toast.LENGTH_SHORT).show()
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