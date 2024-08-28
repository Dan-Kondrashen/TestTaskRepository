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
interface AnimeDAO: BaseDAO<Anime> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addGenreToAnim(item: GenreToAnim)

    @Query("DELETE FROM user_favorite WHERE animeId == :id")
    fun deleteAnimeFromFavorite(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addStudioToAnim(item: StudioToAnim)

    @Query("UPDATE anime SET season = :season, year =:year WHERE rowid = :id")
    fun updateArchiveById(id: Int, season: String, year: Int)

    @Query("SELECT * FROM anime LEFT JOIN anime_to_page ON anime.rowid == animeId " +
            "WHERE anime_to_page.pageIndex == :pageIndex " +
            "ORDER BY score DESC ")
    fun getAnimeMain( pageIndex: Int): List<AnimeMainInfo>

    @Query("SELECT * FROM anime WHERE rowid == :animeId")
    fun getAnimeFullInfoById(animeId: Int): LiveData<AnimeFullInfo>

    @Query("SELECT * FROM user_favorite " +
            "ORDER BY date DESC " +
            "LIMIT :numItems OFFSET :numFrom")
    fun getFavorites(numItems: Int, numFrom: Int): List<Favorite>

    @Query("SELECT * FROM anime LEFT JOIN genre_to_anim ON anime.rowid == genre_to_anim.rowid " +
            "LEFT JOIN genre ON genre_to_anim.genreId == genre.genreId " +
            "JOIN operation_type ON operation_type.rowid = anime.rowid " +
            "WHERE operation_type.name == :searchableStr AND (anime.title LIKE '%' || :searchableStr ||'%' " +
            "OR anime.description LIKE '%' || :searchableStr ||'%' " +
            "OR genre.name LIKE '%' || :searchableStr ||'%') " +
            " GROUP BY anime.rowid ORDER BY operationDate ASC, anime.score DESC LIMIT :offsetItems, 100 ")
    fun getBySearch(offsetItems: Int, searchableStr: String): List<AnimeMainInfo>

    @Query("SELECT * FROM anime INNER " +
            "JOIN user_favorite ON user_favorite.animeId == anime.rowid " +
            "ORDER BY date DESC LIMIT :offset, 25")
    fun getFavoriteAnime(offset: Int): LiveData<List<AnimeFullInfo>>
}