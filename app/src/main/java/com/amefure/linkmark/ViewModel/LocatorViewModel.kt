package com.amefure.linkmark.ViewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.amefure.linkmark.Model.Database.Locator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

class LocatorViewModel(app: Application): RootViewModel(app) {

    private val _locatorList = MutableLiveData<List<Locator>>()
    public val locatorList: LiveData<List<Locator>> = _locatorList


    /**
     * Locator取得
     * MutableLiveDataに格納する
     */
    public fun fetchAllLocator(categoryId: Int) {
        // データの取得は非同期で
        viewModelScope.launch(Dispatchers.IO) {  // データ取得はIOスレッドで
            rootRepository.fetchAllLocator(categoryId) {
                _locatorList.postValue(it.sortedBy { it.order })  // 本来はDBやCacheから取得
            }
        }
    }

    /**
     * Locator追加
     */
    public fun insertLocator(categoryId: Int, title: String, url: String, memo: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val order = rootRepository.getLocatorCount(categoryId)
            rootRepository.insertLocator(
                categoryId = categoryId,
                title = title,
                url = url,
                memo = memo,
                order = order
            )
            fetchAllLocator(categoryId)
        }
    }

    /**
     * [order]のみ更新
     */
    private fun updateOrder(id: Int, order: Int) {
        val locator = _locatorList.value?.find { it.id == id } ?: return@updateOrder
        this.updateLocator(id, locator.categoryId, locator.title, locator.url, locator.memo, order, locator.createdAt)
    }


    /**
     * Locator更新
     */
    public fun updateLocator(id: Int, categoryId: Int, title: String, url: String, memo: String, order: Int, createdAt: Date) {
        viewModelScope.launch(Dispatchers.IO) {
            rootRepository.updateLocator(
                id = id,
                categoryId = categoryId,
                title = title,
                url = url,
                memo = memo,
                order = order,
                createdAt = createdAt)
        }
    }

    /**
     * ローカルデータorderプロパティ更新処理
     */
    public fun changeOrder(source: Int, destination: Int, items: List<Locator>) {

        var locator = items[source]
        var moveId = locator.id

        // 上から下に移動する
        if (source < destination) {
            for (i in (source + 1)..(destination - 1)) {
                items.getOrNull(i)?.let { item ->
                    updateOrder(item.id, item.order - 1)
                }
            }
            updateOrder(moveId, destination - 1)

            // 下から上に移動する
        } else if (destination < source) {
            for (i in (destination until source).reversed()) {
                items.getOrNull(i)?.let { item ->
                    updateOrder(item.id, item.order + 1)
                }
            }
            updateOrder(moveId, destination)
        }
        fetchAllLocator(locator.categoryId)
    }

    public fun deleteLocator(locator: Locator) {
        viewModelScope.launch(Dispatchers.IO) {
            rootRepository.deleteLocator(locator)
        }
    }
}