package br.com.fiap.softmind.data.remote.api

import br.com.fiap.softmind.data.remote.model.MoodRequest
import br.com.fiap.softmind.data.remote.model.MoodResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface MoodService {
    @POST("/mood/daily/recommendations")
    suspend fun getDailyRecommendation(
        @Body request: MoodRequest
    ): Response<MoodResponse>
}