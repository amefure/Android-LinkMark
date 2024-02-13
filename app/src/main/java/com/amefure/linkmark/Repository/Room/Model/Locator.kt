package com.amefure.linkmark.Repository.Room.Model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.net.URL
import java.util.Date

// リレーション
@Entity(tableName = "locator_table",
    foreignKeys = arrayOf(
        ForeignKey(
        entity = Category::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("userId"),
        onDelete =ForeignKey.CASCADE
    ))
)
data class Locator (
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val url: URL,
    val memo: String,
    val order: Int,
    val createdAt: Date
)