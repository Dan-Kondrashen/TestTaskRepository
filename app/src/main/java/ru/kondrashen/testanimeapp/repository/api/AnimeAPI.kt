package ru.kondrashen.testanimeapp.repository.api

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AnimeAPI {
    @GET("top/anime")
    suspend fun getAnimeMain(@Query("page") number: Int): Response<JsonObject>

    @GET("anime/{id}")
    suspend fun getAnimeInfo(@Path("id") id: Int): Response<JsonObject>

    @GET("anime/{id}/full")
    suspend fun getAnimeFullInfo(@Path("id") id: Int): Response<JsonObject>

    @GET("anime")
    suspend fun getAnimeBySearch(@Query("q") info: String,
                                 @Query("order_by") order: String,
                                 @Query("sort") sort: String,
                                 @Query("page") page: Int ): Response<JsonObject>
}