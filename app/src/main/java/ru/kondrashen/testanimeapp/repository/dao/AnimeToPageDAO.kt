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
import ru.kondrashen.testanimeapp.repository.data_class.room_table.AnimeToPage
import ru.kondrashen.testanimeapp.repository.data_class.room_table.Genres

@Dao
interface AnimeToPageDAO: BaseDAO<AnimeToPage> {

}