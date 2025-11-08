package br.com.fiap.softmind.data.remote.api

import br.com.fiap.softmind.data.model.AdminReport
import retrofit2.Response
import retrofit2.http.GET

interface ReportService {
    @GET("/reports")
    suspend fun getAdminReport(): Response<AdminReport>
}