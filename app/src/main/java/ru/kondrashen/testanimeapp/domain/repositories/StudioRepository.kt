package ru.kondrashen.testanimeapp.domain.repositories

import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import ru.kondrashen.testanimeapp.repository.dao.GenresDAO
import ru.kondrashen.testanimeapp.repository.dao.StudiosDAO
import ru.kondrashen.testanimeapp.repository.data_class.room_table.Genres
import ru.kondrashen.testanimeapp.repository.data_class.room_table.Studios
import kotlin.coroutines.CoroutineContext

class StudioRepository(private var studiosDAO: StudiosDAO): CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job

    fun getStudiosByAnimeIdFromRoom(animeId: Int): LiveData<List<Studios>> {
        return studiosDAO.getStudiosByAnimeId(animeId)
    }

}