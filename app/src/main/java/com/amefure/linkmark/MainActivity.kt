package com.amefure.linkmark

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.amefure.linkmark.Repository.Room.Model.Category
import com.amefure.linkmark.Repository.Room.Model.Locator
import com.amefure.linkmark.Repository.Room.RootRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    val rootRepository: RootRepository by lazy { RootRepository(this) }

    var categorys: List<Category>? = null
    var locators: List<Locator>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

            var button1 = findViewById<Button>(R.id.button)
            var button2 = findViewById<Button>(R.id.button1)
            var button3 = findViewById<Button>(R.id.button2)

            var button4 = findViewById<Button>(R.id.button3)
            var button5 = findViewById<Button>(R.id.button4)
            var button6 = findViewById<Button>(R.id.button5)


            button1.setOnClickListener {
                GlobalScope.launch(Dispatchers.IO) {
                    rootRepository.insertCategory("Test","red")
                }
            }

            button2.setOnClickListener {
                GlobalScope.launch(Dispatchers.IO) {
                    categorys = rootRepository.getAllCategory()
                    Log.e("----categorys",categorys.toString())
                }
            }

            button3.setOnClickListener {
                GlobalScope.launch(Dispatchers.IO) {
                    var test = categorys!!.first()
                    rootRepository.deleteCategory(test)
                }
            }

            button4.setOnClickListener {
                GlobalScope.launch(Dispatchers.IO) {
                    var test = categorys!!.first()
                    rootRepository.insertLocator(test.id ,"red","","")
                }
            }

            button5.setOnClickListener {
                GlobalScope.launch(Dispatchers.IO) {
                    locators = rootRepository.getAllLocator()
                    Log.e("----locators",locators.toString())
                    Log.e("----locators",locators!!.first().createdAt.time.toString())
                }
            }

            button6.setOnClickListener {
                GlobalScope.launch(Dispatchers.IO) {
                    var test = locators!!.first()
                    rootRepository.deleteLocator(test)
                }
            }
    }
}