package br.com.fiap.softmind.data.remote

import br.com.fiap.softmind.data.remote.dtos.DailyMoodRequestDto
import br.com.fiap.softmind.data.remote.dtos.DailyMoodResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface MoodApi {
    @POST("/mood/daily/recommendations")
    suspend fun saveMoodAndGetRecommendations(
        @Body request: DailyMoodRequestDto
    ): DailyMoodResponse
}