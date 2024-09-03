package ru.kondrashen.testanimeapp.repository.repositories

import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import ru.kondrashen.testanimeapp.repository.dao.StudiosDAO
import ru.kondrashen.testanimeapp.domain.data_class.room_table.Studios
import kotlin.coroutines.CoroutineContext

class StudioRepository(private var studiosDAO: StudiosDAO): CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job

    fun getStudiosByAnimeIdFromRoom(animeId: Int): LiveData<List<Studios>> {
        return studiosDAO.getStudiosByAnimeId(animeId)
    }

}