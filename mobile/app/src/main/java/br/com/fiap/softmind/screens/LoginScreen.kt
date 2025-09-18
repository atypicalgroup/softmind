package br.com.fiap.softmind.screens

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import br.com.fiap.softmind.componentes.LoginDescription
import br.com.fiap.softmind.componentes.StartButton
import br.com.fiap.softmind.componentes.loginScreen.ClickableTextLink
import br.com.fiap.softmind.componentes.loginScreen.EmailInputField
import br.com.fiap.softmind.componentes.loginScreen.LoginLogo
import br.com.fiap.softmind.componentes.loginScreen.LoginTitle
import br.com.fiap.softmind.componentes.loginScreen.PasswordInputField
import br.com.fiap.softmind.data.remote.ApiClient
import br.com.fiap.softmind.data.remote.model.LoginRequest
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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

        ClickableTextLink(text = "Esqueceu a senha?",
            onClick = {

                //Lógica para abrir a tela de recuperação de senha

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

                                // 👉 Aqui você pode salvar o token em DataStore/SharedPreferences

                                // Navegação de acordo com a role
                                if (email.contains("admin")) {
                                    navController.navigate("AdminScreen")
                                } else {
                                    navController.navigate("EmojiScreen")
                                }
                            } else {
                                Toast.makeText(context, "Erro: resposta inválida", Toast.LENGTH_SHORT).show()
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