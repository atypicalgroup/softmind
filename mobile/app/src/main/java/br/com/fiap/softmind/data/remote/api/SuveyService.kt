package br.com.fiap.softmind.data.remote.api

import br.com.fiap.softmind.data.remote.dtos.SurveyDto
import br.com.fiap.softmind.data.remote.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SurveyService {

    @GET("surveys/daily")
    suspend fun getDailySurvey(): Response<SurveyDto>

    @POST("employees/response/daily")
    suspend fun submitEmployeeDailyResponse(
        @Body request: EmployeeDailyResponseRequest
    ): Response<EmployeeDailyResponse>
}
