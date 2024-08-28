package ru.kondrashen.testanimeapp.repository.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.kondrashen.testanimeapp.repository.data_class.info_classes.AnimeFullInfo
import ru.kondrashen.testanimeapp.repository.data_class.info_classes.AnimeMainInfo
import ru.kondrashen.testanimeapp.repository.data_class.room_table.Anime
import ru.kondrashen.testanimeapp.repository.data_class.room_table.Favorite
import ru.kondrashen.testanimeapp.repository.data_class.relation.GenreToAnim
import ru.kondrashen.testanimeapp.repository.data_class.relation.StudioToAnim
import ru.kondrashen.testanimeapp.repository.data_class.room_table.Genres

@Dao
interface FavoriteDAO: BaseDAO<Favorite> {

    @Query("DELETE FROM user_favorite WHERE animeId == :animeId")
    fun deleteFromFavorite(animeId: Int)

    @Query("SELECT COUNT(DISTINCT anime.rowid) FROM anime INNER " +
            "JOIN user_favorite ON user_favorite.animeId == anime.rowid ")
    fun getFavoriteCountAnime(): LiveData<Int>
}