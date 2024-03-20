package com.amefure.linkmark.Model.Database

import androidx.room.Embedded
import androidx.room.Relation

data class CategoryWithLocators(
    @Embedded val category: Category,
    @Relation(
        parentColumn = "id",
        entityColumn = "category_id"
    )
    val locators: List<Locator>
)