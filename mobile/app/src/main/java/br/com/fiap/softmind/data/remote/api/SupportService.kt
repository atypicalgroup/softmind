package br.com.fiap.softmind.data.remote.api

import br.com.fiap.softmind.data.remote.model.SupportPoint
import retrofit2.Response
import retrofit2.http.GET

interface SupportService {
    @GET("/support-list")
    suspend fun getSupportPoints(): Response<List<SupportPoint>>
}