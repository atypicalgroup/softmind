package br.com.fiap.softmind.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun WeeklyCalendar(
    onDateSelected: (LocalDate) -> Unit
) {
    val today = remember { LocalDate.now() }

    var selectedDate by remember { mutableStateOf(today) }

    val startOfWeek = today.with(DayOfWeek.MONDAY)

    val weekDates = (0..6).map { startOfWeek.plusDays(it.toLong()) }

    val moodEmojis = listOf("😊", "😐", "😞", "😊", "😐", "😎", "😴")

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(1.dp)
    ) {
        weekDates.forEachIndexed { index, date ->
            val isToday = date == today
            val isSelected = date == selectedDate
            val emoji = moodEmojis.getOrNull(index) ?: "🙂"

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        color = when {
                            isSelected -> Color(0xFF4CAF50)
                            isToday -> Color(0xFFBBDEFB)
                            else -> Color.Transparent
                        }
                    )
                    .clickable {
                        selectedDate = date
                        onDateSelected(date)
                    }
                    .padding(8.dp)
            ) {
                Text(
                    text = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale("pt", "BR")),
                    fontFamily = InterFont,
                    fontWeight = FontWeight.ExtraBold,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isSelected) Color.White else Color.Black
                )
                Text(
                    text = date.dayOfMonth.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = InterFont,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) Color.White else Color.Black
                )
                Text(
                    text = emoji,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    modifier = Modifier.padding(top = 1.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WeeklyCalendarPreview() {
    WeeklyCalendar(
        onDateSelected = {}
    )
}
