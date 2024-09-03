package ru.kondrashen.testanimeapp.domain.data_class.room_table

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "anime")
data class Anime(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "rowid")
    val id: Int,
    override var title: String,
    override var description: String?,
    val trailerUrl: String?,
    val type: String?,
    val url: String,
    val source: String,
    val status: String,
    val season: String,
    val score: Float,
    val year: Int?,
    val episodes: Int?,
    val duration: String?,
    val rank: Int?,
): AnimBase(title, description)
