package br.com.fiap.softmind.componentes.emojiScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import br.com.fiap.softmind.R
import br.com.fiap.softmind.ui.theme.CardColor
import br.com.fiap.softmind.ui.theme.CardColor2

@Composable
fun EmojiCardDoctor(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(top = 4.dp, start = 10.dp, bottom = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.emoji_acompanhar),
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = stringResource(R.string.emoji_acompanhar1),
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = stringResource(R.string.emoji_acompanhar2),
                    fontWeight = FontWeight.Bold,
                    color = CardColor2,
                    fontSize = 20.sp,
                    modifier = Modifier.clickable {
                        onClick()
                    }
                )
            }
            Image(
                painter = painterResource(id = R.drawable.man),
                contentDescription = stringResource(R.string.emoji_descrição_medico),
                modifier = Modifier.size(130.dp)
            )
        }
    }
}

@Preview(showBackground = true, locale = "pt-rBR")
@Composable
fun EmojiCardDoctorPreview() {
    val navController = rememberNavController()
    EmojiCardDoctor (onClick = {
        navController.navigate("QuestionScreen")
    })
}