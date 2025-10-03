package br.com.fiap.softmind.componentes

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage // ⬅️ Este é o componente que carrega a URL

@Composable
fun NetworkImageCard(url: String, contentDescription: String) {
    // Componente AsyncImage do Coil para carregar a imagem de uma URL
    AsyncImage(
        model = url, // A URL da imagem (posterPath ou thumbnailUrl)
        contentDescription = contentDescription,
        modifier = Modifier
            .width(160.dp)
            .height(240.dp)
            // Aplica o canto arredondado que você usou no seu código original
            .clip(RoundedCornerShape(12.dp)),
        // Garante que a imagem preencha o espaço sem distorcer (cortando se necessário)
        contentScale = ContentScale.Crop
    )
}