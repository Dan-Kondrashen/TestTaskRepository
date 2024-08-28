package ru.kondrashen.testanimeapp.repository.data_class.relation

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "studio_to_anim", primaryKeys = ["rowid","studioId"])
data class StudioToAnim(
    val rowid: Int,
    val studioId: Int
)
