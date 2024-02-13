package com.amefure.linkmark.Repository.Room.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category_table")
data class Category (
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val color: String,
    val order: Int
)