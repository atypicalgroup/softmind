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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import br.com.fiap.softmind.componentes.loginScreen.EmailInputField
import br.com.fiap.softmind.componentes.LoginDescription
import br.com.fiap.softmind.componentes.loginScreen.LoginLogo
import br.com.fiap.softmind.componentes.loginScreen.LoginTitle
import br.com.fiap.softmind.componentes.StartButton
import br.com.fiap.softmind.helpers.validateOrCreateUser
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope


@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }

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

        Spacer(modifier = Modifier.height(32.dp))

        EmailInputField(email = email, onValueChange = { email = it })

        Spacer(modifier = Modifier.height(32.dp))

        LoginDescription()

        Spacer(modifier = Modifier.height(32.dp))

        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()
        StartButton(onClick = {
            if (email.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                val role = if (email.contains("admin")) "admin" else "funcionario"

                coroutineScope.launch {
                    val user = validateOrCreateUser(email, role, context)
                    Log.d("LOGIN", "Usuário autenticado: ${user.email}, Role: ${user.role}")

                    if (user.role == "admin") {
                        navController.navigate("AdminScreen")
                    } else {
                        navController.navigate("EmojiScreen")
                    }
                }
            } else {
                Toast.makeText(context, "Digite um e-mail válido", Toast.LENGTH_SHORT).show()
            }
        })

        Spacer(modifier = Modifier.weight(0.3f))
    }

}

@Preview(showBackground = true, locale = "pt-rBR")
@Composable
fun LoginScreenPreview() {
    LoginScreen(navController = NavHostController(LocalContext.current))
}