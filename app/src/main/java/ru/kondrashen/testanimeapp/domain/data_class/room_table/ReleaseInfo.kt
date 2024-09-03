package ru.kondrashen.testanimeapp.domain.data_class.room_table

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "release_info")
data class ReleaseInfo (
    @PrimaryKey(autoGenerate = true)
    val releaseId: Int,
    val from: String,
    val to: String?
)