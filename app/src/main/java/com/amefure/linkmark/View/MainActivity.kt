package com.amefure.linkmark.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.amefure.linkmark.R
import com.amefure.linkmark.Repository.DataStore.DataStoreRepository
import com.amefure.linkmark.View.AppLock.AppLockFragment
import com.amefure.linkmark.View.Category.CategoryListFragment
import com.amefure.linkmark.ViewModel.AppLockViewModel
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private val dataStoreRepository = DataStoreRepository(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 初期化
        MobileAds.initialize(this)

        lifecycleScope.launch {
            var result = dataStoreRepository.observeAppLockPassword().first()
            if (result == null || result == 0) {
                supportFragmentManager.beginTransaction().apply {
                    add(R.id.main_frame, CategoryListFragment())
                    addToBackStack(null)
                    commit()
                }
            } else {
                supportFragmentManager.beginTransaction().apply {
                    add(R.id.main_frame, AppLockFragment())
                    addToBackStack(null)
                    commit()
                }
            }
        }
    }
}