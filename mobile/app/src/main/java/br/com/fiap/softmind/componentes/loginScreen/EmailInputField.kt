package br.com.fiap.softmind.componentes.loginScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults.outlinedTextFieldColors
import androidx.compose.material3.Icon
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.runtime.LaunchedEffect
import br.com.fiap.softmind.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailInputField(
    email: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    var hasInteracted by remember { mutableStateOf(false) }
    val isEmailValid = isValidEmail(email)
    val showErrorMessage = hasInteracted && !isEmailValid
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = email,
            onValueChange = { newValue ->
                onValueChange(newValue)
                if (!hasInteracted && newValue.isNotEmpty()) {
                    hasInteracted = true
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
            singleLine = true,
            leadingIcon = {
                Icon(
                    Icons.Filled.Email,
                    contentDescription = stringResource(R.string.icone_email),
                    tint = Color(0xFF333333)
                )
            },
            placeholder = { Text(text = stringResource(R.string.placeholder_email)) },
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
                .focusRequester(focusRequester)
        )
        if (showErrorMessage) {
            Text(
                text = stringResource(R.string.email_invalido),
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

fun isValidEmail(email: String): Boolean {
    if (email.isEmpty()) return true
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

@Preview(showBackground = true)
@Composable
fun EmailInputFieldPreview() {
    var email by remember { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        EmailInputField(
            email = email,
            onValueChange = {
                email = it
            }
        )
    }
}

