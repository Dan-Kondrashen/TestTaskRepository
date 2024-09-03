package ru.kondrashen.testanimeapp.repository.dao

import androidx.room.Dao
import androidx.room.Query
import ru.kondrashen.testanimeapp.domain.data_class.room_table.UserOperationType

@Dao
interface OperationTypeDAO: BaseDAO<UserOperationType> {
    @Query("DELETE FROM operation_type WHERE name== :search")
    fun clearLastOperation(search: String)
}