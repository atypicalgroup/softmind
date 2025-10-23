package br.com.fiap.softmind.data.remote

import br.com.fiap.softmind.data.remote.api.AuthService
import br.com.fiap.softmind.data.remote.api.MoodService
import br.com.fiap.softmind.data.remote.api.SurveyService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

object ApiClient {
    private const val BASE_URL = "http://10.0.2.2:8000/"

    var loggedUserId: String? = null
    var authToken: String? = null
    var loggedUserName: String? = null
    var loggedUsername: String? = null

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor(AuthInterceptor { authToken })
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val authService: AuthService = retrofit.create(AuthService::class.java)
    val moodService: MoodService = retrofit.create(MoodService::class.java)
    val surveyService: SurveyService = retrofit.create(SurveyService::class.java)
    

}
