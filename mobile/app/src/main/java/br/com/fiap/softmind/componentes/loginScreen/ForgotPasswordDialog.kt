import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults.outlinedTextFieldColors
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.fiap.softmind.R
import br.com.fiap.softmind.componentes.loginScreen.isValidEmail

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordDialog(
    onDismissRequest: () -> Unit,
    onConfirm: (email: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var hasInteracted by remember { mutableStateOf(false) }
    val isEmailValid = isValidEmail(email)
    val showErrorMessage = hasInteracted && !isEmailValid
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(stringResource(R.string.redefinir_senha),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column (horizontalAlignment = Alignment.CenterHorizontally){
                Text(stringResource(R.string.inserir_email_resete),
                    textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
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
                    label = { Text(stringResource(R.string.email)) },
                    shape = RoundedCornerShape(36.dp),
                    colors = TextFieldDefaults.run{
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
                                start = 16.dp,
                                top = 4.dp,
                                end = 16.dp
                            )
                    )
                }

            }
        },


        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                TextButton(
                    onClick = onDismissRequest
                ) {
                    Text(stringResource(R.string.cancelar))
                }

                Spacer(modifier = Modifier.width(8.dp))

                TextButton(
                    onClick = {
                        onConfirm(email)
                    }
                ) {
                    Text(stringResource(R.string.enviar))
                }
            }
        },
    )
}

@Preview(showBackground = true, locale = "pt-rBR")
@Composable
fun ForgotPasswordDialogPreview() {
    ForgotPasswordDialog(
        onDismissRequest = {},
        onConfirm = {}
    )
}