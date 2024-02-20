package com.amefure.linkmark.ViewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.amefure.linkmark.Model.Locator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LocatorViewModel(app: Application): RootViewModel(app) {

    private val _locatorList = MutableLiveData<List<Locator>>()
    public val locatorList: LiveData<List<Locator>> = _locatorList

    public fun fetchAllLocator(categoryId: Int) {
        // データの取得は非同期で
        viewModelScope.launch(Dispatchers.IO) {  // データ取得はIOスレッドで
            rootRepository.fetchAllLocator(categoryId) {
                _locatorList.postValue(it)  // 本来はDBやCacheから取得
            }
        }
    }

    public fun insertLocator(categoryId: Int, title: String, url: String, memo: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val order = _locatorList.value?.size ?: 0
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

//    public fun updateCategory(id: Int, name:String, returnFlag: Boolean ,current: Boolean, amountSum: Long) {
//        viewModelScope.launch(Dispatchers.IO) {
//            rootRepository.updateBorrower(id,name,returnFlag,current, amountSum)
//        }
//    }

    public fun deleteLocator(locator: Locator) {
        viewModelScope.launch(Dispatchers.IO) {
            rootRepository.deleteLocator(locator)
        }
    }
}