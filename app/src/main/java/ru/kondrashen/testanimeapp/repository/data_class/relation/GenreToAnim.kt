package ru.kondrashen.testanimeapp.repository.data_class.relation

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "genre_to_anim", primaryKeys = ["rowid","genreId"])
data class GenreToAnim(
    val rowid: Int,
    val genreId: Int
)
