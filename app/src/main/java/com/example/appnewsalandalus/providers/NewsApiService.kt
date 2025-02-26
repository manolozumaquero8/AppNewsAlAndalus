package com.example.appnewsalandalus.providers

import com.example.appnewsalandalus.models.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("v2/everything")
    suspend fun getEverything(
        @Query("q") query: String,
        @Query("language") language: String = "es",
        @Query("apiKey") apiKey: String
    ): Response<NewsResponse>
}








