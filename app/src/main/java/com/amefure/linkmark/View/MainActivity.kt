package com.amefure.linkmark.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import com.amefure.linkmark.R
import com.amefure.linkmark.View.Category.CategoryListFragment
import com.google.android.gms.ads.MobileAds


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 初期化
        MobileAds.initialize(this)

        supportFragmentManager.beginTransaction().apply {
            add(R.id.main_frame, CategoryListFragment())
            addToBackStack(null)
            commit()
        }
    }

}