package ru.kondrashen.testanimeapp.domain.data_class.room_table

import androidx.room.Entity

@Entity(tableName = "anime_to_page", primaryKeys = ["pageIndex", "animeId"])
data class AnimeToPage (
    var pageIndex: Int,
    var animeId: Int
)