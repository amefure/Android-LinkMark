package com.amefure.linkmark.Model

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
    val title: String,
    val url: String,
    val memo: String,
    val order: Int,
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