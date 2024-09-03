package ru.kondrashen.testanimeapp.repository.repositories

import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import ru.kondrashen.testanimeapp.repository.dao.AnimeDAO
import ru.kondrashen.testanimeapp.repository.dao.FavoriteDAO
import ru.kondrashen.testanimeapp.domain.data_class.info_classes.AnimeFullInfo
import ru.kondrashen.testanimeapp.domain.data_class.room_table.Favorite
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