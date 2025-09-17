package br.com.fiap.softmind.componentes

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.softmind.R

@Composable
fun EndTrophy(){
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.trofeu),
                contentDescription = "Troféu",
                modifier = Modifier.size(55.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = stringResource(id = R.string.parabens3)
                        + " 21% com relação aos seus colegas.",
                color = Color(0xFF989898),
                fontSize = 15.sp,
                fontFamily = InterFont
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun EndTrophyPreview(){
    EndTrophy()
}