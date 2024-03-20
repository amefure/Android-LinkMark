package com.amefure.linkmark.ViewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.amefure.linkmark.Model.Database.Category
import com.amefure.linkmark.Model.Database.CategoryWithLocators
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryViewModel(app: Application) : RootViewModel(app) {

    private val _categoryWithLocators = MutableLiveData<List<CategoryWithLocators>>()
    public val categoryWithLocators: LiveData<List<CategoryWithLocators>> = _categoryWithLocators


    /**
     * Category取得
     * MutableLiveDataに格納する
     */
    public fun fetchAllCategorys() {
        // データの取得は非同期で
        viewModelScope.launch(Dispatchers.IO) {  // データ取得はIOスレッドで
            rootRepository.fetchAllCategory {
                _categoryWithLocators.postValue(it.sortedBy { it.category.order })  // 本来はDBやCacheから取得
            }
        }
    }

    /**
     * Category追加
     */
    public fun insertCategory(name: String, color: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val order = rootRepository.getCategoryCount()
            rootRepository.insertCategory(name, color, order)
            fetchAllCategorys()
        }
    }

    /**
     * [order]のみ更新
     */
    private fun updateOrder(id: Int, order: Int) {
        val categoryWithLocators = _categoryWithLocators.value?.find { it.category.id == id } ?: return@updateOrder
        this.updateCategory(id, categoryWithLocators.category.name , categoryWithLocators.category.color, order)
    }


    /**
     * Category更新
     */
    public fun updateCategory(id: Int, name: String, color: String, order: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            rootRepository.updateCategory(id, name ,color, order)
        }
    }

    /**
     * ローカルデータorderプロパティ更新処理
     */
    public fun changeOrder(source: Int, destination: Int, items: List<CategoryWithLocators>) {

        var moveId = items[source].category.id

        // 上から下に移動する
        if (source < destination) {
            for (i in (source + 1)..(destination - 1)) {
                items.getOrNull(i)?.let { item ->
                    updateOrder(item.category.id, item.category.order - 1)
                }
            }
            updateOrder(moveId, destination - 1)

            // 下から上に移動する
        } else if (destination < source) {
            for (i in (destination until source).reversed()) {
                items.getOrNull(i)?.let { item ->
                    updateOrder(item.category.id, item.category.order + 1)
                }
            }
            updateOrder(moveId, destination)
        }
        fetchAllCategorys()
    }

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