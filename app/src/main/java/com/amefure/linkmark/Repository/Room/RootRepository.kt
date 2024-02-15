package com.amefure.linkmark.Repository.Room

import android.content.Context
import com.amefure.linkmark.Repository.Room.Database.AppDatabase
import com.amefure.linkmark.Model.Category
import com.amefure.linkmark.Model.Locator
import com.amefure.linkmark.ViewModel.RootApplication
import io.reactivex.disposables.CompositeDisposable
import java.util.Date


class RootRepository (context: Context) {

    // Dao
    private val categoryDao = AppDatabase.getDatabase(context).categoryDao()
    private val locatorDao = AppDatabase.getDatabase(context).locatorDao()
    private  var dd = context.applicationContext as RootApplication
    // カテゴリ追加
    public fun insertCategory(name: String, color: String, order: Int) {
        val category = Category(
            id = 0,
            name = name,
            color = color,
            order = order
        )
        categoryDao.insertCategory(category)
    }

    // URL追加
    public fun insertLocator(categoryId: Int,title: String, url: String, memo: String) {
        val locator = Locator(
            id = 0,
            title = title,
            url = url,
            memo = memo,
            order = 0,
            createdAt = Date(),
            categoryId = categoryId,
        )
        locatorDao.insertLocator(locator)
    }

    // カテゴリ取得
    public fun fetchAllCategory(): List<Category> {
        return categoryDao.fetchAllCategory()
    }

    // URL取得
    public fun fetchAllLocator(): List<Locator> {
        return locatorDao.fetchAllLocator()
    }

    // カテゴリ削除
    public fun deleteCategory(category: Category) {
        categoryDao.deleteCategory(category)
    }

    // URL削除
    public fun deleteLocator(locator: Locator) {
        locatorDao.deleteLocator(locator)
    }
}