package ru.kondrashen.testanimeapp.repository.data_class.room_table

import androidx.room.Entity
import ru.kondrashen.testanimeapp.repository.data_class.SimpleNameInterface
import java.sql.Date


@Entity(tableName = "operation_type", primaryKeys = ["name", "rowid"])
data class UserOperationType(
    override var name: String,
    var operationDate: Date,
    val rowid: Int,
):SimpleNameInterface
