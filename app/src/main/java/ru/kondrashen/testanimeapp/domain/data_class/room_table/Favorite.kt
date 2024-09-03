package ru.kondrashen.testanimeapp.domain.data_class.room_table

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity(tableName = "user_favorite")
data class Favorite(
    @PrimaryKey(autoGenerate = false)
    val animeId: Int,
    val date: Date
)
