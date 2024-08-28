package ru.kondrashen.testanimeapp.repository.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface BaseDAO<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addItem(item: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addItems(vararg item: T)

    @Update
    suspend fun updateItem(item: T)

    @Delete
    suspend fun deleteItem(item: T)
}