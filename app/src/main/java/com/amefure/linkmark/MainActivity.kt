package com.amefure.linkmark

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.amefure.linkmark.Repository.Room.Dao.AppDao
import com.amefure.linkmark.Repository.Room.Database.AppDatabase
import com.amefure.linkmark.Repository.Room.Model.Category

class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var db : AppDatabase
        lateinit var dao : AppDao
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = AppDatabase.getDatabase(this)
        dao = db.appDao()
    }
}