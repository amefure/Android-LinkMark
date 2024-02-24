package com.amefure.linkmark.Repository.Room.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.amefure.linkmark.Model.Category
import io.reactivex.Flowable

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertCategory(category: Category)

    @Update
    fun updateCategory(category: Category)

    @Query("SELECT * FROM category_table")
    fun fetchAllCategory(): Flowable<List<Category>>

    @Query("SELECT COUNT(*) FROM category_table")
    fun getCount(): Int

    @Delete
    fun deleteCategory(category: Category)
}