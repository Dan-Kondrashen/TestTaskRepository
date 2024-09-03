package ru.kondrashen.testanimeapp.domain.data_class.room_table

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "title")
data class TitleTable (
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    val type: String,
    val title: String
)