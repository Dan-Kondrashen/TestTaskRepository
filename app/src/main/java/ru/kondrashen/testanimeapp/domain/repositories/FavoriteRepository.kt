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
import ru.kondrashen.testanimeapp.repository.dao.FavoriteDAO
import ru.kondrashen.testanimeapp.repository.dao.GenresDAO
import ru.kondrashen.testanimeapp.repository.dao.StudiosDAO
import ru.kondrashen.testanimeapp.repository.data_class.info_classes.AnimeFullInfo
import ru.kondrashen.testanimeapp.repository.data_class.info_classes.AnimeMainInfo
import ru.kondrashen.testanimeapp.repository.data_class.room_table.AnimBase
import ru.kondrashen.testanimeapp.repository.data_class.room_table.Anime
import ru.kondrashen.testanimeapp.repository.data_class.relation.GenreToAnim
import ru.kondrashen.testanimeapp.repository.data_class.relation.StudioToAnim
import ru.kondrashen.testanimeapp.repository.data_class.room_table.Favorite
import ru.kondrashen.testanimeapp.repository.data_class.room_table.Genres
import ru.kondrashen.testanimeapp.repository.data_class.room_table.Studios
import java.sql.Date
import java.util.Date as utilDate
import kotlin.coroutines.CoroutineContext

class FavoriteRepository(private var animeDAO: AnimeDAO, private var favoriteDAO: FavoriteDAO): CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job


    fun getFavoriteAnimeFromRoom(pageIndex: Int): LiveData<List<AnimeFullInfo>> {
        val numItems = 25
        val offsset = numItems * (pageIndex - 1)
        return animeDAO.getFavoriteAnime(offsset)
    }
    fun getFavoriteCountFromRoom(): LiveData<Int>{
        return favoriteDAO.getFavoriteCountAnime()
    }

    fun addToFavoriteRoom(itemId: Int) {
        launch (Dispatchers.IO){
            favoriteDAO.addItem(Favorite( animeId =itemId, date = Date(utilDate().time)))
        }
    }
    fun deleteFromFavoriteRoom(itemId: Int) {
        launch (Dispatchers.IO){
            favoriteDAO.deleteFromFavorite(itemId)
        }
    }
}