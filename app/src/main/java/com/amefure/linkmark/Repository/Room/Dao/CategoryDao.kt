package com.amefure.linkmark.Repository.Room.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.amefure.linkmark.Repository.Room.Model.Category

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertCategory(category: Category)

    @Query("SELECT * FROM category_table")
    fun getAllCategory(): List<Category>

    @Delete
    fun deleteCategory(category: Category)
}