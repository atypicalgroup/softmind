package br.com.fiap.softmind.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import br.com.fiap.softmind.R
import br.com.fiap.softmind.componentes.EndButton
import br.com.fiap.softmind.componentes.EndRecommendation
import br.com.fiap.softmind.componentes.EndTrophy
import br.com.fiap.softmind.componentes.HeaderEnd

@Composable
fun EndScreen(navController: NavController) {
    val posters = listOf(
        R.drawable.forrestposter,
        R.drawable.insideout2poster,
        R.drawable.whiplashposter,
        R.drawable.insideout1poster
    )
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFEEF0F6))
            .padding(horizontal = 16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(35.dp))

            HeaderEnd()

            Spacer(modifier = Modifier.height(16.dp))

            EndTrophy()

            Spacer(modifier = Modifier.height(16.dp))

            EndRecommendation()

            Spacer(modifier = Modifier.height(16.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(posters) { poster ->
                    Image(
                        painter = painterResource(id = poster),
                        contentDescription = "Filme recomendado",
                        modifier = Modifier
                            .width(160.dp)
                            .height(240.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            EndButton(navController = navController)
        }
    }
}


@Preview(showBackground = true, locale = "pt-rBR")
@Composable
fun EndScreenPreview() {
    EndScreen(navController = NavHostController(LocalContext.current))
}
