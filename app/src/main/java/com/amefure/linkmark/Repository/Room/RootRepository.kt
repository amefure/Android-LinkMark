package com.amefure.linkmark.Repository.Room

import android.content.Context
import com.amefure.linkmark.Repository.Room.Database.AppDatabase
import com.amefure.linkmark.Model.Database.Category
import com.amefure.linkmark.Model.Database.Locator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import java.util.Date


class RootRepository (context: Context) {

    // Dao
    private val categoryDao = AppDatabase.getDatabase(context).categoryDao()
    private val locatorDao = AppDatabase.getDatabase(context).locatorDao()

    // ゴミ箱
    private val compositeDisposable = CompositeDisposable()

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
    public fun insertLocator(categoryId: Int, title: String, url: String, memo: String, order: Int) {
        val locator = Locator(
            id = 0,
            title = title,
            url = url,
            memo = memo,
            order = order,
            createdAt = Date(),
            categoryId = categoryId,
        )
        locatorDao.insertLocator(locator)
    }

    // カテゴリ追加
    public fun updateCategory(id: Int, name: String, color: String, order: Int) {
        val category = Category(
            id = id,
            name = name,
            color = color,
            order = order
        )
        categoryDao.updateCategory(category)
    }

    // URL追加
    public fun updateLocator(id: Int, categoryId: Int, title: String, url: String, memo: String, order: Int, createdAt: Date) {
        val locator = Locator(
            id = id,
            title = title,
            url = url,
            memo = memo,
            order = order,
            createdAt = createdAt,
            categoryId = categoryId,
        )
        locatorDao.updateLocator(locator)
    }

    // カテゴリ取得
    public fun fetchAllCategory(callback: (List<Category>) -> Unit) {
        categoryDao.fetchAllCategory()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { categorys ->
                    callback(categorys)
                }
            ).addTo(compositeDisposable)
    }


    // URL取得
    public fun fetchAllLocator(categoryId: Int, callback: (List<Locator>) -> Unit) {
        locatorDao.fetchAllLocator(categoryId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { locators ->
                    callback(locators)
                }
            ).addTo(compositeDisposable)
    }

    // リストの個数取得
    public fun getCategoryCount(): Int {
        return categoryDao.getCount()
    }

    public fun getLocatorCount(categoryId: Int): Int {
        return locatorDao.getCount(categoryId)
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