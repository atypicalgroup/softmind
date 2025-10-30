package br.com.fiap.softmind.screens.surveyEmployee

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import br.com.fiap.softmind.R
import br.com.fiap.softmind.componentes.EndButton
import br.com.fiap.softmind.componentes.EndRecommendation
import br.com.fiap.softmind.componentes.HeaderEnd
import br.com.fiap.softmind.componentes.emojiScreen.SupportPointsButton
import br.com.fiap.softmind.componentes.emojiScreen.SupportPointsSection
import br.com.fiap.softmind.viewmodel.MoodViewModel
import br.com.fiap.softmind.viewmodel.SupportViewModel
import coil.compose.AsyncImage

@Composable
fun SuggestionScreen(
    navController: NavController,
    moodViewModel: MoodViewModel = viewModel(),
    supportViewModel: SupportViewModel = viewModel()
) {
    // 🎥 Dados das recomendações (filmes e atividades)
    val movies by moodViewModel.movies.collectAsState()
    val activities by moodViewModel.activities.collectAsState()

    val supportPoints by supportViewModel.supportPoints.collectAsState()
    val isLoading by supportViewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        if (movies.isEmpty() && activities.isEmpty()) {
            moodViewModel.loadRecommendations(emoji = "😊", feeling = "Motivado")
        }
        if (supportPoints.isEmpty()) {
            supportViewModel.loadSupportPoints()
        }
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
            EndRecommendation()

            // 🎬 Filmes recomendados
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Filmes Recomendados",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
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

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Atividades Físicas",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(activities) { activity ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .width(200.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .padding(8.dp)
                    ) {
                        AsyncImage(
                            model = activity.thumbnailUrl,
                            contentDescription = activity.title,
                            modifier = Modifier
                                .width(200.dp)
                                .height(120.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = activity.title,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 2
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            SupportPointsSection()


            Spacer(modifier = Modifier.height(12.dp))
            EndButton(navController = navController)
        }
    }
}

@Preview(showBackground = true, locale = "pt-rBR")
@Composable
fun SugestionScreenPreview() {
    SuggestionScreen(navController = NavController(LocalContext.current))
}
