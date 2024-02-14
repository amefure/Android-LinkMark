package com.amefure.linkmark.Repository.Room.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.amefure.linkmark.Repository.Room.Model.Category
import com.amefure.linkmark.Repository.Room.Model.Locator

@Dao
interface AppDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertCategory(category: Category)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertLocator(locator: Locator)

    @Delete
    fun deleteCategory(category: Category)

    @Delete
    fun deleteLocator(locator: Locator)
}