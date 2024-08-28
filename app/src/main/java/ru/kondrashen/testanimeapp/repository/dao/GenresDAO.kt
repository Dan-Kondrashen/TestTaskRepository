package ru.kondrashen.testanimeapp.repository.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import ru.kondrashen.testanimeapp.repository.data_class.info_classes.AnimeMainInfo
import ru.kondrashen.testanimeapp.repository.data_class.room_table.Anime
import ru.kondrashen.testanimeapp.repository.data_class.room_table.Favorite
import ru.kondrashen.testanimeapp.repository.data_class.room_table.Genres

@Dao
interface GenresDAO: BaseDAO<Genres> {
    @Query("SELECT genre.genreId, genre.name FROM genre INNER JOIN genre_to_anim ON genre_to_anim.genreId == genre.genreId WHERE genre_to_anim.rowid == :animId GROUP BY genre.genreId")
    fun getGenreByAnimeId(animId: Int): LiveData<List<Genres>>

}