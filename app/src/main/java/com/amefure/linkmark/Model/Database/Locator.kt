package com.amefure.linkmark.Model.Database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.util.Date

// リレーション
@Entity(tableName = "locator_table",
    foreignKeys = arrayOf(
        ForeignKey(
        entity = Category::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("category_id"),
        onDelete =ForeignKey.CASCADE
    ))
)
data class Locator (
    @PrimaryKey(autoGenerate = true) val id: Int,
    var title: String,
    var url: String,
    var memo: String,
    var order: Int,
    @ColumnInfo(name = "created_at")
    val createdAt: Date,
    @ColumnInfo(name = "category_id")
    val categoryId: Int
)

class DateConverters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?) : Long? {
        return date?.time
    }
}