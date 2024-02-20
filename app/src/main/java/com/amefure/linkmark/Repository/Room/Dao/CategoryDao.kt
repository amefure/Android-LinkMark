package com.amefure.linkmark.Repository.Room.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.amefure.linkmark.Model.Category
import io.reactivex.Flowable

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertCategory(category: Category)

    @Query("SELECT * FROM category_table")
    fun fetchAllCategory(): Flowable<List<Category>>


    @Delete
    fun deleteCategory(category: Category)
}