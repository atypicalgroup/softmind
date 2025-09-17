package br.com.fiap.softmind.data.remote.api

import br.com.fiap.softmind.data.remote.model.LoginRequest
import br.com.fiap.softmind.data.remote.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}