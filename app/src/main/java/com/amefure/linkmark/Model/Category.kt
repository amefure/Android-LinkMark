package com.amefure.linkmark.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category_table")
data class Category (
    @PrimaryKey(autoGenerate = true) val id: Int,
    var name: String,
    var color: String,
    var order: Int
)