package ru.kondrashen.testanimeapp.domain.data_class.relation

import androidx.room.Entity

@Entity(tableName = "studio_to_anim", primaryKeys = ["rowid","studioId"])
data class StudioToAnim(
    val rowid: Int,
    val studioId: Int
)
