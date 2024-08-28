package ru.kondrashen.testanimeapp.repository.data_class.info_classes

import androidx.room.Embedded
import androidx.room.Relation
import ru.kondrashen.testanimeapp.repository.data_class.room_table.Anime
import ru.kondrashen.testanimeapp.repository.data_class.room_table.Genres

interface AnimeBase {
    val anime: Anime
}
