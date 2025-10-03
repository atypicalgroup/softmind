package br.com.fiap.softmind.screens

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController

import br.com.fiap.softmind.componentes.EndButton
import br.com.fiap.softmind.componentes.EndRecommendation
import br.com.fiap.softmind.componentes.EndTrophy
import br.com.fiap.softmind.componentes.HeaderEnd
import br.com.fiap.softmind.componentes.NetworkImageCard
import br.com.fiap.softmind.viewmodel.EndViewModel
import br.com.fiap.softmind.viewmodel.MoodViewModel

import coil.compose.AsyncImage

@Composable
fun EndScreen(navController: NavController, viewModel: EndViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val movies by viewModel.movies.collectAsState()

    // carrega recomendações ao abrir
    LaunchedEffect(Unit) {
        viewModel.loadRecommendations("😀", "feliz")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFEEF0F6))
            .padding(horizontal = 16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(35.dp))

            HeaderEnd()
            Spacer(modifier = Modifier.height(16.dp))
            EndTrophy()
            Spacer(modifier = Modifier.height(16.dp))
            EndRecommendation()
            Spacer(modifier = Modifier.height(16.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(movies) { movie ->
                    AsyncImage(
                        model = movie.posterUrl,
                        contentDescription = movie.title,
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