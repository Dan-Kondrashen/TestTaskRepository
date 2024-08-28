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
import ru.kondrashen.testanimeapp.repository.data_class.room_table.AnimeFTS
import ru.kondrashen.testanimeapp.repository.data_class.room_table.Genres

@Dao
interface AnimeVirtualTableDAO: BaseDAO<AnimeFTS> {


    @Query("SELECT * FROM anime JOIN anime_fts ON anime.rowid == anime_fts.rowid LEFT JOIN genre_to_anim ON anime_fts.rowid == genre_to_anim.rowid " +
            "LEFT JOIN genre ON genre_to_anim.genreId == genre.genreId " +
            "WHERE anime_fts.title MATCH :searchableStr ||'%' " +
            "OR anime_fts.description LIKE '%' || :searchableStr ||'%' " +
            "OR genre.name LIKE '%' || :searchableStr ||'%' " +
            "GROUP BY anime_fts.rowid ORDER BY anime.score DESC LIMIT :numItems OFFSET :numFrom")
    fun getBySearch(searchableStr: String, numItems: Int, numFrom: Int): List<AnimeMainInfo>
}