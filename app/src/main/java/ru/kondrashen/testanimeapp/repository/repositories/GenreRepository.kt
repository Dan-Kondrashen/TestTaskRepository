package ru.kondrashen.testanimeapp.repository.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import ru.kondrashen.testanimeapp.repository.api.ApiFactory
import ru.kondrashen.testanimeapp.repository.dao.GenresDAO
import ru.kondrashen.testanimeapp.domain.data_class.info_classes.AnimeMainInfo
import ru.kondrashen.testanimeapp.domain.data_class.room_table.Genres
import kotlin.coroutines.CoroutineContext

class GenreRepository(private var genresDAO: GenresDAO): CoroutineScope {

    private val genreApi = ApiFactory.genreApi
    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job

    fun getGenresFromServ(): LiveData<String>{
        var queryResult = MutableLiveData<String>()
        launch (Dispatchers.IO) {
            try {
                val resp = genreApi.getAllGenres()
                if (resp.isSuccessful) {
                    val jsonObject = resp.body()
                    jsonObject?.let { jObject ->
                        val dataJson = jObject.getAsJsonArray("data")
                        for (index in 0..< dataJson.size()){
                            val genreJson = dataJson.get(index).asJsonObject
                            val genre = Genres(
                                genreId = genreJson.get("mal_id").asInt,
                                name = genreJson.get("name").asString
                            )
                            genresDAO.addItem(genre)
                        }
                    }
                }
            }
            catch (e: Exception){
                e.printStackTrace()
                queryResult.postValue("fail")
            }
        }
        return queryResult
    }

    fun getGenreByAnimeIdFromRoom(animeId: Int): LiveData<List<Genres>> {
        return genresDAO.getGenreByAnimeId(animeId)
    }

}