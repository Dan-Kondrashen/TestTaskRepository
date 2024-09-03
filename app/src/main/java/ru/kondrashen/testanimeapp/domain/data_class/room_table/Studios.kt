package ru.kondrashen.testanimeapp.domain.data_class.room_table

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.kondrashen.testanimeapp.domain.data_class.SimpleNameInterface

@Entity(tableName = "studio")
data class Studios(
    @PrimaryKey(autoGenerate = true)
    val studioId: Int,
    override var name: String
): SimpleNameInterface
