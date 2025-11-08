package br.com.fiap.softmind.componentes.admScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import br.com.fiap.softmind.R
import br.com.fiap.softmind.componentes.InterFont
import br.com.fiap.softmind.componentes.WeeklyCalendar
import br.com.fiap.softmind.viewmodel.EngagementViewModel

@Composable
fun AdmCalendar(
    navController: NavController,
    viewModel: EngagementViewModel = viewModel()
) {
    Box() {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
        ) {
            OutlinedButton(
                onClick = { viewModel.loadAdminReport() },
                shape = RoundedCornerShape(50),
                border = BorderStroke(1.dp, Color.Transparent),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color(0xFFD9D9D9)
                )
            ) {
                Text(
                    text = stringResource(id = R.string.emocionometro),
                    fontFamily = InterFont,
                    color = (Color(0xFF727272)),
                )
            }
            WeeklyCalendar(onDateSelected = {})
        }
    }
}

@Preview(showSystemUi = true, locale = "pt-rBR")
@Composable
fun AdmCalendarPreview() {
    AdmCalendar(navController = NavController(context = LocalContext.current))
}