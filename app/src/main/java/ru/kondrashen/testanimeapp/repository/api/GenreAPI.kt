package ru.kondrashen.testanimeapp.repository.api

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GenreAPI {
    @GET("genres/anime")
    suspend fun getAllGenres(): Response<JsonObject>


}