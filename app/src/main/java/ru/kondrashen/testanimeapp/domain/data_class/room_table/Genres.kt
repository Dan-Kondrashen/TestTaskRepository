package ru.kondrashen.testanimeapp.domain.data_class.room_table

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.kondrashen.testanimeapp.domain.data_class.SimpleNameInterface

@Entity(tableName = "genre")
data class Genres(
    @PrimaryKey(autoGenerate = true)
    val genreId: Int,
    override var name: String
): SimpleNameInterface
