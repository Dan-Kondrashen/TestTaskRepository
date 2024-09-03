package ru.kondrashen.testanimeapp.repository.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import ru.kondrashen.testanimeapp.domain.data_class.room_table.Favorite

@Dao
interface FavoriteDAO: BaseDAO<Favorite> {

    @Query("DELETE FROM user_favorite WHERE animeId == :animeId")
    fun deleteFromFavorite(animeId: Int)

    @Query("SELECT COUNT(DISTINCT anime.rowid) FROM anime INNER " +
            "JOIN user_favorite ON user_favorite.animeId == anime.rowid ")
    fun getFavoriteCountAnime(): LiveData<Int>
}