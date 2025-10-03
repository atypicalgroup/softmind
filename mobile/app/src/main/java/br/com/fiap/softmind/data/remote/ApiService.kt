package br.com.fiap.softmind.data.remote

import br.com.fiap.softmind.data.remote.dtos.DailyMoodRequestDto
import br.com.fiap.softmind.data.remote.dtos.DailyMoodResponse
import br.com.fiap.softmind.data.remote.model.LoginRequest
import br.com.fiap.softmind.data.remote.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService{

    @GET("v3/9b0bf6b2-d432-44a0-8575-937fcee4395f")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>


}