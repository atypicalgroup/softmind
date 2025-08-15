package br.com.fiap.softmind.componentes.loginScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.com.fiap.softmind.R
@Composable
fun LoginLogo(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.logo_softmind),
        contentDescription = stringResource(id = R.string.logo_softmind),
        modifier = modifier
            .size(120.dp)
    )
}