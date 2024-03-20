package com.amefure.linkmark.Model.Database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "category_table")
data class Category (
    @PrimaryKey(autoGenerate = true) val id: Int,
    var name: String,
    var color: String,
    var order: Int
)