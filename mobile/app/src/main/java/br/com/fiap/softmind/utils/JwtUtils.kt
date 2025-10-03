package br.com.fiap.softmind.utils

import android.util.Base64
import org.json.JSONObject

object JwtUtils {
    fun decodePayload(token: String): JSONObject? {
        return try {
            val parts = token.split(".")
            if (parts.size < 2) return null
            val payload = String(Base64.decode(parts[1], Base64.URL_SAFE))
            JSONObject(payload)
        } catch (e: Exception) {
            null
        }
    }
}
