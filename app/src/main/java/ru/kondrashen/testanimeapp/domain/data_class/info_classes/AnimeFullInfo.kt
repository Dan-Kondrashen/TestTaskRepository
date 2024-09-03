package ru.kondrashen.testanimeapp.domain.data_class.info_classes

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import ru.kondrashen.testanimeapp.domain.data_class.relation.GenreToAnim
import ru.kondrashen.testanimeapp.domain.data_class.room_table.Anime
import ru.kondrashen.testanimeapp.domain.data_class.room_table.Favorite
import ru.kondrashen.testanimeapp.domain.data_class.room_table.Genres

data class AnimeFullInfo (

    @Embedded override val anime: Anime,
    @Relation(parentColumn = "rowid",
        entityColumn = "genreId",
        associateBy = Junction(GenreToAnim::class)
    )
    val animGenres: List<Genres>?,
    @Relation(parentColumn = "rowid",
        entityColumn = "animeId",
    )
    val favorite: Favorite?


): AnimeBase
