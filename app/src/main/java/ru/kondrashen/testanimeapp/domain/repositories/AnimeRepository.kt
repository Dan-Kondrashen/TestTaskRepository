package ru.kondrashen.testanimeapp.domain.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import ru.kondrashen.testanimeapp.repository.api.ApiFactory
import ru.kondrashen.testanimeapp.repository.dao.AnimeDAO
import ru.kondrashen.testanimeapp.repository.dao.AnimeToPageDAO
import ru.kondrashen.testanimeapp.repository.dao.AnimeVirtualTableDAO
import ru.kondrashen.testanimeapp.repository.dao.GenresDAO
import ru.kondrashen.testanimeapp.repository.dao.OperationTypeDAO
import ru.kondrashen.testanimeapp.repository.dao.StudiosDAO
import ru.kondrashen.testanimeapp.repository.data_class.info_classes.AnimeFullInfo
import ru.kondrashen.testanimeapp.repository.data_class.info_classes.AnimeMainInfo
import ru.kondrashen.testanimeapp.repository.data_class.room_table.AnimBase
import ru.kondrashen.testanimeapp.repository.data_class.room_table.Anime
import ru.kondrashen.testanimeapp.repository.data_class.relation.GenreToAnim
import ru.kondrashen.testanimeapp.repository.data_class.relation.StudioToAnim
import ru.kondrashen.testanimeapp.repository.data_class.responses.MainResponse
import ru.kondrashen.testanimeapp.repository.data_class.room_table.AnimeToPage
import ru.kondrashen.testanimeapp.repository.data_class.room_table.Genres
import ru.kondrashen.testanimeapp.repository.data_class.room_table.Studios
import ru.kondrashen.testanimeapp.repository.data_class.room_table.UserOperationType
import java.sql.Date
import kotlin.coroutines.CoroutineContext

class AnimeRepository(private var animeDAO: AnimeDAO,
                      private var genresDAO: GenresDAO,
                      private var studiosDAO: StudiosDAO,
                      private var animeVertDAO: AnimeVirtualTableDAO,
                      private var pageDAO: AnimeToPageDAO,
                      private var operationTypeDAO: OperationTypeDAO): CoroutineScope {

    private val animeApi = ApiFactory.animeApi
    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job

    fun getAnimeMainPageFromServ(pageIndex: Int): LiveData<MainResponse> {
        var queryResult = MutableLiveData<MainResponse>()
        launch (Dispatchers.IO){
            try {
                val resp = animeApi.getAnimeMain(pageIndex)
                if (resp.isSuccessful) {

                    val jsonObject = resp.body()
                    jsonObject?.let { startObj ->
                        val dataJson = startObj.getAsJsonArray("data")
                        val paginationJson = startObj.getAsJsonObject("pagination")
                        val pageNum =
                            if (paginationJson.get("current_page").isJsonNull)
                                null
                            else paginationJson.get("current_page").asInt
                        val maxPage =
                            if (paginationJson.get("last_visible_page").isJsonNull)
                                null
                            else paginationJson.get("last_visible_page").asInt

                        for (index in 0..< dataJson.size()){
                            val animeInfoJson = dataJson.get(index).asJsonObject
                            val images= animeInfoJson.get("images").asJsonObject
                            val youtubeJson= animeInfoJson.get("trailer").asJsonObject
                            val url = images.get("jpg").asJsonObject.get("image_url").asString
                            val anime = Anime(
                                id = animeInfoJson.get("mal_id").asInt,
                                title = if (animeInfoJson.get("title_english").isJsonNull) {
                                    animeInfoJson.get("title").asString
                                }
                                else{
                                    animeInfoJson.get("title_english").asString
                                },
                                description = if (animeInfoJson.get("synopsis").isJsonNull) null else animeInfoJson.get("synopsis").asString,
                                type = if (animeInfoJson.get("type").isJsonNull) null else animeInfoJson.get("type").asString,
                                url = url,
                                source = animeInfoJson.get("source").asString,
                                status = animeInfoJson.get("status").asString,
                                score = if (animeInfoJson.get("score").isJsonNull) 0f else animeInfoJson.get("score").asFloat,
                                year = if (animeInfoJson.get("year").isJsonNull) null else animeInfoJson.get("year").asInt,
                                episodes = if (animeInfoJson.get("episodes").isJsonNull) null else animeInfoJson.get("episodes").asInt,
                                duration = if (animeInfoJson.get("duration").isJsonNull) "" else animeInfoJson.get("duration").asString,
                                rank = if (animeInfoJson.get("rank").isJsonNull) null else animeInfoJson.get("rank").asInt,
                                trailerUrl = if (youtubeJson.get("url").isJsonNull) null else youtubeJson.get("embed_url").asString,
                                season = if (animeInfoJson.get("season").isJsonNull) "" else animeInfoJson.get("season").asString)

                            animeDAO.addItem(anime)
                            pageDAO.addItem(AnimeToPage(pageIndex = pageIndex, animeId = anime.id))
                        }
                        queryResult.postValue(MainResponse(status = "success",
                            code = resp.code(),
                            maxPage = maxPage,
                            curPage = pageNum
                        ))
                    }
                }
            }
            catch (e: Exception){
                e.printStackTrace()
                queryResult.postValue(MainResponse(status = "fail", code = 503,
                    null, null))
            }
        }
        return queryResult
    }

    fun getAnimeFullByIdFromServ(id: Int): LiveData<String>{
        var queryResult = MutableLiveData<String>()
        launch {
            try {
                val resp = animeApi.getAnimeFullInfo(id)
                if (resp.isSuccessful) {
                    val animeFullJson = resp.body()
                    animeFullJson?.let {
                        var dataJson = it.get("data").asJsonObject
                        var genresJsonArray = dataJson.get("genres").asJsonArray
                        var studiosJsonArray = dataJson.get("studios").asJsonArray
                        for (genre in genresJsonArray){
                            val genreObj = genre as JsonObject
                            val genreItem = Genres(
                                genreId = genreObj.get("mal_id").asInt,
                                name = genreObj.get("name").asString
                            )
                            genresDAO.addItem(genreItem)
                            animeDAO.addGenreToAnim(
                                GenreToAnim(
                                genreId = genreItem.genreId,
                                rowid =  id)
                            )
                        }
                        for (studio in studiosJsonArray){
                            val studioObj = studio as JsonObject
                            val studioItem = Studios(
                                studioId = studioObj.get("mal_id").asInt,
                                name = studioObj.get("name").asString
                            )
                            studiosDAO.addItem(studioItem)
                            animeDAO.addStudioToAnim(
                                StudioToAnim(
                                studioId = studioItem.studioId,
                                rowid =  id)
                            )
                        }
                    }
                    queryResult.postValue("success")
                    animeDAO
                }
                else
                    queryResult.postValue("fail")
            }
            catch (e: Exception){
                e.printStackTrace()
                queryResult.postValue("fail")
            }
        }
        return queryResult
    }

    fun getAnimeMainPageFromRoom(pageIndex: Int): List<AnimeMainInfo> {
        return animeDAO.getAnimeMain(pageIndex)
    }

    fun getAnimeBySearchFromServ(pageIndex: Int, text: String): LiveData<MainResponse> {
        var queryResult = MutableLiveData<MainResponse>()
        launch {
            try {
                val resp = animeApi.getAnimeBySearch(text, "score", "desc", pageIndex)
                if (resp.isSuccessful) {
                    val queryDate = Date(java.util.Date().time)
                    val jsonObject = resp.body()

                    jsonObject?.let { startObj ->
                        val dataJson = startObj.getAsJsonArray("data")
                        val paginationJson = startObj.getAsJsonObject("pagination")
                        val pageNum =
                            if (paginationJson.get("current_page").isJsonNull)
                                null
                            else paginationJson.get("current_page").asInt
                        val maxPage =
                            if (paginationJson.get("last_visible_page").isJsonNull)
                                null
                            else paginationJson.get("last_visible_page").asInt
                        val itemsInfoJson = paginationJson.get("items").asJsonObject
                        val total =
                            if (itemsInfoJson.get("total").isJsonNull)
                                null
                            else itemsInfoJson.get("total").asInt
                        if (dataJson.size() != 0 && pageIndex ==1) operationTypeDAO.clearLastOperation(text)
                        for (index in 0..< dataJson.size()){
                            val animeInfoJson = dataJson.get(index).asJsonObject
                            val images= animeInfoJson.get("images").asJsonObject
                            val youtubeJson= animeInfoJson.get("trailer").asJsonObject
                            val genresJsonArray = animeInfoJson.get("genres").asJsonArray
                            val url = images.get("jpg").asJsonObject.get("image_url").asString
                            val anime = Anime(
                                id = animeInfoJson.get("mal_id").asInt,
                                title = if (animeInfoJson.get("title_english").isJsonNull) {
                                    animeInfoJson.get("title").asString
                                }
                                else{
                                    animeInfoJson.get("title_english").asString
                                    },
                                description = if (animeInfoJson.get("synopsis").isJsonNull) null else animeInfoJson.get("synopsis").asString,
                                type = if (animeInfoJson.get("type").isJsonNull) null else animeInfoJson.get("type").asString,
                                url = url,
                                source = animeInfoJson.get("source").asString,
                                status = animeInfoJson.get("status").asString,
                                score = if (animeInfoJson.get("score").isJsonNull) 0f else animeInfoJson.get("score").asFloat,
                                year = if (animeInfoJson.get("year").isJsonNull) null else animeInfoJson.get("year").asInt,
                                episodes = if (animeInfoJson.get("episodes").isJsonNull) null else animeInfoJson.get("episodes").asInt,
                                duration = if (animeInfoJson.get("duration").isJsonNull) "" else animeInfoJson.get("duration").asString,
                                rank = if (animeInfoJson.get("rank").isJsonNull) null else animeInfoJson.get("rank").asInt,
                                trailerUrl = if (youtubeJson.get("url").isJsonNull) null else youtubeJson.get("embed_url").asString,
                                season = if (animeInfoJson.get("season").isJsonNull) "" else animeInfoJson.get("season").asString)
                            animeDAO.addItem(anime)
                            operationTypeDAO.addItem(UserOperationType(name = text, operationDate = queryDate, rowid = anime.id))
                            for (genre in genresJsonArray){
                                val genreObj = genre as JsonObject
                                animeDAO.addGenreToAnim(
                                    GenreToAnim(
                                        genreId = genreObj.get("mal_id").asInt,
                                        rowid =  animeInfoJson.get("mal_id").asInt)
                                )
                            }

                        }
                        queryResult.postValue(MainResponse(status = "success",
                            code = resp.code(),
                            maxPage = maxPage,
                            curPage = pageNum
                        ))
                    }
                }
                else{
                    queryResult.postValue(
                        MainResponse(status = "fail", code = 503,
                            null, null))
                }
            }
            catch (e: Exception){
                e.printStackTrace()
                queryResult.postValue(
                    MainResponse(status = "fail", code = 503,
                        null, null))
            }
        }
        return queryResult
    }

    fun getAnimeBySearchFromRoom(offsetIndex: Int, text: String): List<AnimeMainInfo> {
        return animeDAO.getBySearch(offsetIndex, text)
    }
// Оставлено на будущее!!!
//    fun getAnimeFTSBySearchFromRoom(pageIndex: Int, text: String): List<AnimeFTS> {
//        val numItems = 25
//        val offsset = numItems * (pageIndex - 1)
//        return animeVertDAO.getBySearch( )
//    }

    fun deleteAnimeFromFavorite(id: Int){
        launch {
            animeDAO.deleteAnimeFromFavorite(id)
        }
    }

    fun getChosenAnimeInfoFromRoom(animeId: Int): LiveData<AnimeFullInfo> {

        return animeDAO.getAnimeFullInfoById(animeId)
    }
}