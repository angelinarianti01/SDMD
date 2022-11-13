package com.example.stockmanagement4

import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.viewpager2.widget.ViewPager2
import com.example.stockmanagement4.adapters.ViewPagerAdapter
import com.example.stockmanagement4.databasehelpers.HistoryHelper
import com.example.stockmanagement4.databasehelpers.PQHelper
import com.example.stockmanagement4.databasehelpers.ProductHelper
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Database
        val productHelper : ProductHelper = ProductHelper(this)
        val PQHelper : PQHelper = PQHelper(this)
        val historyHelper : HistoryHelper = HistoryHelper(this)

        //PQHelper.dropTable()
        //PQHelper.createTable()
        // productTable.createTable()
        //productQuantityTable.dropTable()
        //productQuantityTable.createTable()
        //PQHelper.addRecord(ProductQuantity("1", "2022-11-06", 9))
        //productTable.addProduct(Product(id="",name="Milk", category="Coffee", brand="A2", cost=5, watch=0, minstock=10))
        //myDB.deleteAllData()
        //myDB.updateData(Product(id="3",name="beanos", category="Coffee", brand="Lavazza", cost=20, watch=1, minstock=10))
        historyHelper.deleteAllData()
        //historyHelper.createTable()
        //historyHelper.addRecord(History("1", "2022-11-06", -10))


        val cursor : Cursor = PQHelper.readAllData()
        while (cursor.moveToNext()) {
            Log.i("result", cursor.getString(0))
            Log.i("result", cursor.getString(1))
            Log.i("result", cursor.getString(2))
        }


        // Viewpager and tabs
        val viewPager2=findViewById<ViewPager2>(R.id.view_pager_2)
        val adapter= ViewPagerAdapter(supportFragmentManager,lifecycle)
        viewPager2.adapter=adapter

        val tabLayout=findViewById<TabLayout>(R.id.tab_layout)
        TabLayoutMediator(tabLayout,viewPager2){tab,position->
            when(position){
                0->{
                    tab.text="Home"
                }
                1->{
                    tab.text="Products"
                }
                2->{
                    tab.text="History"
                }
                3-> {
                    tab.text="Watchlist"
                }
            }
        }.attach()
    }

    public fun navigateFragment(position: Int) {
        val viewPager2=findViewById<ViewPager2>(R.id.view_pager_2)
        viewPager2.currentItem = position
    }

}