package com.amefure.linkmark.Repository.Room.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.amefure.linkmark.Model.Locator
import io.reactivex.Flowable

@Dao
interface LocatorDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertLocator(locator: Locator)

     @Query("SELECT * FROM locator_table WHERE category_id = :categoryId")
     fun fetchAllLocator(categoryId: Int): Flowable<List<Locator>>

    @Delete
    fun deleteLocator(locator: Locator)
}