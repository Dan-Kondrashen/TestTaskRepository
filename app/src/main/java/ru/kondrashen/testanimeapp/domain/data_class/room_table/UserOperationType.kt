package ru.kondrashen.testanimeapp.domain.data_class.room_table

import androidx.room.Entity
import ru.kondrashen.testanimeapp.domain.data_class.SimpleNameInterface
import java.sql.Date


@Entity(tableName = "operation_type", primaryKeys = ["name", "rowid"])
data class UserOperationType(
    override var name: String,
    var operationDate: Date,
    val rowid: Int,
): SimpleNameInterface
