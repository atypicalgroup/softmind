package br.com.fiap.softmind.data.remote.api

import br.com.fiap.softmind.data.remote.model.FirstLoginResetRequest
import br.com.fiap.softmind.data.remote.model.ForgotPasswordRequest
import br.com.fiap.softmind.data.remote.model.LoginRequest
import br.com.fiap.softmind.data.remote.model.LoginResponse
import br.com.fiap.softmind.data.remote.model.ResetPasswordRequest
import br.com.fiap.softmind.data.remote.model.ValidateCodeRequest
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<ResponseBody>

    @POST("/auth/forgot-password")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): Response<Unit>

    @POST("/auth/verify-token")
    suspend fun validateToken(@Body request: ValidateCodeRequest): Response<Unit>

    @POST("/auth/change-password")
    suspend fun changePassword(@Body request: ResetPasswordRequest): Response<Unit>

    @POST("/auth/change-password-first-access")
    suspend fun changePasswordFirstAccess(@Body request: FirstLoginResetRequest): Response<Unit>


}