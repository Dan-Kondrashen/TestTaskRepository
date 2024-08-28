package ru.kondrashen.testanimeapp.repository.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiFactory {
    const val url = "https://api.jikan.moe/v4/"
    private val gsonBuilder = GsonBuilder()
    private var okHttpClient = OkHttpClient().newBuilder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .build()
    private var retrofit = Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
        .build()
    val animeApi = retrofit.create(AnimeAPI::class.java)
    val genreApi = retrofit.create(GenreAPI::class.java)
}