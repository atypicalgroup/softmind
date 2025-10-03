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
import br.com.fiap.softmind.data.remote.model.LoginRequest
import br.com.fiap.softmind.utils.JwtUtils
import kotlinx.coroutines.launch

@SuppressLint("StringFormatInvalid")
@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // 1. ADICIONADO: Estado para controlar a visibilidade do pop-up
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // 2. ADICIONADO: Bloco que exibe o pop-up se 'showDialog' for verdadeiro
    if (showDialog) {
        ForgotPasswordDialog(
            onDismissRequest = {
                showDialog = false
            },
            onConfirm = { emailParaReset ->

                val logMessage = context.getString(R.string.msg_redefinicao_senha, emailParaReset)
                Log.d("RESET_SENHA", logMessage)

                val toastMessage = context.getString(R.string.msg_brinde_redefinicao_senha, emailParaReset)
                Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show()

                // TODO: Aqui você chamaria seu ViewModel para fazer a requisição à API de reset

                showDialog = false
            }
        )
    }

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

                showDialog = true

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