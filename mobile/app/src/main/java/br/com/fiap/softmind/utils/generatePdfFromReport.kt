package br.com.fiap.softmind.utils

import android.content.Context
import android.graphics.*
import android.graphics.pdf.PdfDocument
import br.com.fiap.softmind.R
import br.com.fiap.softmind.data.model.AdminReport
import br.com.fiap.softmind.screens.administrative.mapEmoji
import java.io.File
import java.io.FileOutputStream

fun generatePdfFromReport(context: Context, report: AdminReport): File? {
    val document = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
    val page = document.startPage(pageInfo)
    val canvas = page.canvas

    val paint = Paint().apply {
        color = Color.BLACK
        textSize = 16f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
    }

    val boldPaint = Paint().apply {
        color = Color.BLACK
        textSize = 18f
        typeface = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD)
    }

    // 🔹 Logo Softmind
    val logoBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.logo_softmind)
    val resizedLogo = Bitmap.createScaledBitmap(logoBitmap, 100, 100, true)
    canvas.drawBitmap(resizedLogo, 40f, 40f, null)

    // 🔹 Cabeçalho
    canvas.drawText("Relatório Semanal Softmind", 160f, 70f, boldPaint)
    canvas.drawText("Resumo de bem-estar emocional", 160f, 95f, paint)
    canvas.drawText("Gerado automaticamente pelo aplicativo", 160f, 115f, paint)

    var y = 250

    // 🔹 Dados
    val periodo = "${report.startOfWeek ?: "--"} a ${report.endOfWeek ?: "--"}"
    val engajamento = report.weekSummary?.overallEngagement ?: 0.0
    val bemEstar = report.currentHealthyPercentage ?: 0.0
    val sentimento = report.moodSummary?.mostCommonMood
    val alerta = report.alerts?.joinToString(", ") ?: "Nenhum alerta registrado"

    canvas.drawText("Período: $periodo", 40f, y.toFloat(), paint)
    y += 30
    canvas.drawText("Engajamento médio: ${String.format("%.2f", engajamento)}%", 40f, y.toFloat(), paint)
    y += 30
    canvas.drawText("Bem-estar atual: ${String.format("%.2f", bemEstar)}%", 40f, y.toFloat(), paint)
    y += 30
    if (sentimento != null) {
        canvas.drawText("Sentimento mais comum: ${mapEmoji(sentimento)}", 40f, y.toFloat(), paint)
        y += 30
    }
    canvas.drawText("Alertas da semana:", 40f, y.toFloat(), boldPaint)
    y += 24
    alerta.chunked(80).forEach {
        canvas.drawText(it, 60f, y.toFloat(), paint)
        y += 22
    }

    // 🔹 Rodapé
    canvas.drawText("Softmind © ${java.time.Year.now()}", 230f, 800f, paint)

    document.finishPage(page)

    val pdfFile = File(context.cacheDir, "relatorio_admin.pdf")

    return try { // ✅ O RETURN ESTÁ DENTRO DO TRY E FUNCIONA
        FileOutputStream(pdfFile).use { output ->
            document.writeTo(output)
        }
        document.close()
        pdfFile  // ✅ <<<<<< RETORNA O ARQUIVO CORRETAMENTE
    } catch (e: Exception) {
        e.printStackTrace()
        document.close()
        null
    }
}
