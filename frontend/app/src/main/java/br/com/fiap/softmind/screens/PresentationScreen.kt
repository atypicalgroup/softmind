package br.com.fiap.softmind.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import br.com.fiap.softmind.R
import br.com.fiap.softmind.componentes.InterFont
import br.com.fiap.softmind.componentes.presentationScreen.ConteudoTela
import br.com.fiap.softmind.componentes.presentationScreen.LanguageToggleButton
import br.com.fiap.softmind.componentes.presentationScreen.NomeApp
import kotlinx.coroutines.delay

@Composable
fun PresentationScreen(navController: NavController) {
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(2000)
        isVisible = true
    }
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(500))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.prescreenbg),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Column(
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(25.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NomeApp()

                    LanguageToggleButton()
                }
                Spacer(modifier = Modifier.padding(190.dp))

                ConteudoTela(modifier = Modifier)

                Spacer(modifier = Modifier.padding(14.dp))

                OutlinedButton(
                    onClick = { navController.navigate("LoginScreen") },
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .align(Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(
                        width = 2.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF94D567), Color(0xFF63BEC1))
                        )
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.iniciar),
                        fontSize = 16.sp,
                        fontFamily = InterFont,
                        color = Color.White

                    )
                }
                Spacer(modifier = Modifier.padding(50.dp))
            }
        }
    }
}

@Preview(showSystemUi = true, locale = "pt-rBR")
@Composable
fun PresentationScreenPreview() {
    PresentationScreen(navController = NavHostController(LocalContext.current))
}
