package dev.raduhs.newsapp.network

import dev.raduhs.newsapp.models.TopNewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {

    @GET("top-headlines")
    suspend fun getTopArticles(@Query("country") country: String): TopNewsResponse

    @GET("top-headlines")
    suspend fun getArticlesByCategory(@Query("category") category: String): TopNewsResponse

    @GET("everything")
    fun getArticlesBySource(@Query("sources") source: String): Call<TopNewsResponse>

    @GET("everything")
    fun getArticles(@Query("q") query: String): Call<TopNewsResponse>

}