package com.amefure.linkmark.ViewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.amefure.linkmark.Model.Locator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

class LocatorViewModel(app: Application): RootViewModel(app) {

    private val _locatorList = MutableLiveData<List<Locator>>()
    public val locatorList: LiveData<List<Locator>> = _locatorList

    public fun fetchAllLocator(categoryId: Int) {
        // データの取得は非同期で
        viewModelScope.launch(Dispatchers.IO) {  // データ取得はIOスレッドで
            rootRepository.fetchAllLocator(categoryId) {
                _locatorList.postValue(it.sortedBy { it.order })  // 本来はDBやCacheから取得
            }
        }
    }

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

    public fun deleteLocator(locator: Locator) {
        viewModelScope.launch(Dispatchers.IO) {
            rootRepository.deleteLocator(locator)
        }
    }
}