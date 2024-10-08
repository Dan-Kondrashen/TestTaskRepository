package ru.kondrashen.testanimeapp.repository.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import ru.kondrashen.testanimeapp.domain.data_class.room_table.Studios

@Dao
interface StudiosDAO: BaseDAO<Studios> {
    @Query("SELECT * FROM studio INNER JOIN studio_to_anim ON studio_to_anim.studioId == studio.studioId WHERE studio_to_anim.rowid == :animId")
    fun getStudiosByAnimeId(animId: Int): LiveData<List<Studios>>
}