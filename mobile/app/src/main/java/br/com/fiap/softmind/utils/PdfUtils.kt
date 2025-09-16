package br.com.fiap.softmind.utils

import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.net.Uri
import androidx.core.content.FileProvider
import br.com.fiap.softmind.R
import br.com.fiap.softmind.data.model.Engagement
import br.com.fiap.softmind.screens.mapEmoji
import java.io.File
import java.io.FileOutputStream

fun generatePdfFromEngagement(context: Context, engagement: Engagement): File? {
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

    val logoBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.logo_softmind)
    val resizedLogo = Bitmap.createScaledBitmap(logoBitmap, 100, 100, true)
    canvas.drawBitmap(resizedLogo, 40f, 40f, null)

    canvas.drawText("Relatório de Engajamento", 160f, 70f, boldPaint)
    canvas.drawText("Relatório resumido de bem-estar emocional", 160f, 95f, paint)
    canvas.drawText("Esta é uma prévia gerada automaticamente", 160f, 115f, paint)

    var y = 250


    canvas.drawText("Período: ${engagement.periodo}", 40f, y.toFloat(), paint)
    y += 30
    canvas.drawText("Engajamento: ${engagement.engajamento_colaboradores}%", 40f, y.toFloat(), paint)
    y += 30
    canvas.drawText("Bem-estar emocional: ${engagement.bem_estar_emocional}%", 40f, y.toFloat(), paint)
    y += 30
    canvas.drawText("Variação: ${engagement.variacao_percentual}% (${engagement.tendencia})", 40f, y.toFloat(), paint)
    y += 30
    canvas.drawText("Sentimento: ${mapEmoji(engagement.emoji_representativo)}", 40f, y.toFloat(), paint)
    y += 40

    engagement.comentario.chunked(80).forEach {
        canvas.drawText(it, 40f, y.toFloat(), paint)
        y += 24
    }

    document.finishPage(page)

    val pdfFile = File(context.cacheDir, "relatorio_engajamento.pdf")
    return try {
        FileOutputStream(pdfFile).use { document.writeTo(it) }
        document.close()
        pdfFile
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}



fun openPdfFile(context: Context, file: File) {
    val uri: Uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        file
    )

    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, "application/pdf")
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
    }

    context.startActivity(intent)
}