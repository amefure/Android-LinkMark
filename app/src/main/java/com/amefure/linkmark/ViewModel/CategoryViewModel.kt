package com.amefure.linkmark.ViewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.amefure.linkmark.Model.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryViewModel(app: Application) : RootViewModel(app) {

    private val _categoryList = MutableLiveData<List<Category>>()
    public val categoryList: LiveData<List<Category>> = _categoryList


    public fun fetchAllCategorys() {
        // データの取得は非同期で
        viewModelScope.launch(Dispatchers.IO) {  // データ取得はIOスレッドで
            var data = rootRepository.fetchAllCategory()
            _categoryList.postValue(data)  // 本来はDBやCacheから取得
        }
    }

    public fun insertCategory(name: String, color: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val order = _categoryList.value?.size ?: 0
            rootRepository.insertCategory(name, color, order)
        }
    }

//    public fun updateCategory(id: Int, name:String, returnFlag: Boolean ,current: Boolean, amountSum: Long) {
//        viewModelScope.launch(Dispatchers.IO) {
//            rootRepository.updateBorrower(id,name,returnFlag,current, amountSum)
//        }
//    }


    /**
     * リレーションを設定しているため
     * 親である[Category]を削除すると
     * 紐づいている[Locator]も自動で削除される
     */
    public fun deleteCategory(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            rootRepository.deleteCategory(category)
        }
    }
}