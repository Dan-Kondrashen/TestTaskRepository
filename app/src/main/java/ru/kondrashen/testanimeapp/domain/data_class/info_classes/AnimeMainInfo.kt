package ru.kondrashen.testanimeapp.domain.data_class.info_classes

import androidx.room.Embedded
import androidx.room.Relation
import ru.kondrashen.testanimeapp.domain.data_class.room_table.Anime
import ru.kondrashen.testanimeapp.domain.data_class.room_table.Genres

data class AnimeMainInfo (

    @Embedded override val anime: Anime,
    @Relation(parentColumn = "rowid",
        entityColumn = "genreId",
    )
    val animGenres: List<Genres>?
): AnimeBase
