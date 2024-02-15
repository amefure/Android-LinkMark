package com.amefure.linkmark.ViewModel

import android.app.Application
import com.amefure.linkmark.Repository.Room.RootRepository

class RootApplication : Application() {
    /**
     * [RootRepository]のインスタンス
     */
    val rootRepository: RootRepository by lazy { RootRepository(this) }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onTerminate() {
        super.onTerminate()
    }
}