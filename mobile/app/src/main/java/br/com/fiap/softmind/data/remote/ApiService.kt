package br.com.fiap.softmind.data.remote

import br.com.fiap.softmind.data.model.EngagementResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService{

    @GET("v3/9b0bf6b2-d432-44a0-8575-937fcee4395f")
    suspend fun getEngagement(): Response<EngagementResponse>

}