package br.com.fiap.softmind.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.fiap.softmind.R
import br.com.fiap.softmind.ui.theme.SoftBlue
import br.com.fiap.softmind.ui.theme.SoftGreen
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    val scale = remember { Animatable(1.2f) }
    val alpha = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            alpha.animateTo(1f, animationSpec = tween(1000))
            scale.animateTo(1f, animationSpec = tween(800))

            alpha.animateTo(0f, animationSpec = tween(800))
            scale.animateTo(1.2f, animationSpec = tween(800))
            onTimeout()
        }
    }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            SoftGreen, SoftBlue
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.white_logo),
                contentDescription = stringResource(R.string.logo_softmind),
                modifier = Modifier.size(width = 250.dp, height = 200.dp)
            )
        }
    }

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(onTimeout = {2000})
}

