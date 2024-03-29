package com.amefure.linkmark.Repository.Room.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.amefure.linkmark.Model.Database.Locator
import io.reactivex.Flowable

@Dao
interface LocatorDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertLocator(locator: Locator)

    @Update
    fun updateLocator(locator: Locator)

    @Query("SELECT * FROM locator_table WHERE category_id = :categoryId")
    fun fetchAllLocator(categoryId: Int): Flowable<List<Locator>>

    @Query("SELECT COUNT(*) FROM locator_table WHERE category_id = :categoryId")
    fun getCount(categoryId: Int): Int

    @Delete
    fun deleteLocator(locator: Locator)
}