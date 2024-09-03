package ru.kondrashen.testanimeapp.repository.dao

import androidx.room.Dao
import ru.kondrashen.testanimeapp.domain.data_class.room_table.AnimeToPage

@Dao
interface AnimeToPageDAO: BaseDAO<AnimeToPage> {

}