package com.amefure.linkmark.Repository.Room.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.amefure.linkmark.Model.Database.Category
import com.amefure.linkmark.Model.Database.CategoryWithLocators
import io.reactivex.Flowable

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertCategory(category: Category)

    @Update
    fun updateCategory(category: Category)

    @Query("SELECT * FROM category_table")
    fun fetchAllCategory(): Flowable<List<Category>>

    @Transaction
    @Query("SELECT * FROM category_table")
    fun fetchCategoriesWithLocators(): Flowable<List<CategoryWithLocators>>

    @Query("SELECT COUNT(*) FROM category_table")
    fun getCount(): Int

    @Delete
    fun deleteCategory(category: Category)
}