package com.amefure.linkmark.Repository.Room.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.amefure.linkmark.Repository.Room.Model.Locator

@Dao
interface LocatorDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertLocator(locator: Locator)

    @Query("SELECT * FROM locator_table")
    fun getAllLocator(): List<Locator>

    @Delete
    fun deleteLocator(locator: Locator)
}