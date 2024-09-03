package ru.kondrashen.testanimeapp.domain.data_class.relation

import androidx.room.Entity

@Entity(tableName = "genre_to_anim", primaryKeys = ["rowid","genreId"])
data class GenreToAnim(
    val rowid: Int,
    val genreId: Int
)
