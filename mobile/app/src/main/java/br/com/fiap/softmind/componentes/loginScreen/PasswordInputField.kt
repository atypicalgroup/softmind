package br.com.fiap.softmind.componentes.loginScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults.outlinedTextFieldColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.fiap.softmind.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordInputField(
    password: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    var passwordVisible by remember { mutableStateOf(false) }
    var hasInteracted by remember { mutableStateOf(false) }
    val isPasswordValid = isValidPassword(password)
    val showErrorMessage = hasInteracted && !isPasswordValid

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = password,
            onValueChange = { newValue ->
                onValueChange(newValue)
                if (!hasInteracted && newValue.isNotEmpty()) {
                    hasInteracted = true
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            singleLine = true,
            // Oculta ou mostra os caracteres da senha
            visualTransformation = if (passwordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            leadingIcon = {
                Icon(
                    Icons.Filled.Lock,
                    contentDescription = stringResource(R.string.icone_senha),
                    tint = Color(0xFF333333)
                )
            },
            // Ícone de visibilidade à direita para mostrar/ocultar senha
            trailingIcon = {
                val image = if (passwordVisible) {
                    Icons.Filled.Visibility
                } else {
                    Icons.Filled.VisibilityOff
                }

                val description = if (passwordVisible) stringResource(R.string.ocultar_senha)
                else stringResource(R.string.mostrar_senha)

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = description)
                }
            },
            placeholder = { Text(text = stringResource(R.string.placeholder_senha)) },
            shape = RoundedCornerShape(36.dp),
            colors = TextFieldDefaults.run {
                outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF00BFA5),
                    unfocusedBorderColor = Color(0xFFD9D9D9),
                    cursorColor = Color(0xFF00BFA5),
                    unfocusedContainerColor = Color(0xFFF0F0F0),
                    errorBorderColor = Color(0xFFFF5252),
                    errorCursorColor = Color(0xFFFF5252)
                )
            },
            isError = showErrorMessage,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        )
        // Mensagem de erro para senha inválida
        if (showErrorMessage) {
            Text(
                text = stringResource(R.string.senha_invalida),
                color = Color(0xFFFF5252),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 48.dp,
                        top = 4.dp,
                        end = 32.dp
                    )
            )
        }
    }
}

// Função de validação para a senha (ex: mínimo de 6 caracteres)
fun isValidPassword(password: String): Boolean {
    if (password.isEmpty()) return true // Não mostra erro se o campo estiver vazio inicialmente
    return password.length >= 6
}

@Preview(showBackground = true)
@Composable
fun PasswordInputFieldPreview() {
    var password by remember { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        PasswordInputField(
            password = password,
            onValueChange = {
                password = it
            }
        )
    }
}