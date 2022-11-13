package com.example.stockmanagement4.fragments

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stockmanagement4.ProductActivity
import com.example.stockmanagement4.R
import com.example.stockmanagement4.adapters.ProductListAdapter
import com.example.stockmanagement4.databasehelpers.HistoryHelper
import com.example.stockmanagement4.databasehelpers.ProductHelper
import com.example.stockmanagement4.models.Product
import com.google.android.material.floatingactionbutton.FloatingActionButton


class ListFragment() : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onResume() {
        super.onResume()

        // Reload the view to update records or text colour
        onCreateView(layoutInflater, view as ViewGroup?, arguments)

        // Passed data
        val filter = arguments?.getString("filter").toString()

        // Views
        val title = view?.findViewById<TextView>(R.id.listTitle)
        val addFab = view?.findViewById<FloatingActionButton>(R.id.addFab)

        // Set title, buttons, and cursor based on filter
        val productHelper : ProductHelper = ProductHelper(context)
        lateinit var cursor: Cursor

        when(filter){
            // Showing list of products
            ""->{
                // Set title
                title?.text = "My Products"

                // Set add Product button
                addFab?.setOnClickListener {
                    val intent = Intent(context, ProductActivity::class.java)
                    intent.putExtra("activity", "addProduct")
                    startActivity(intent)
                }

                // Set cursor from the product table
                cursor = productHelper.readAllData()
            }
            // Show list of products that are being watched
            "watchlist"->{
                // Set title
                title?.text = "Watched Products"

                // Set add button to invisible
                addFab?.visibility = View.GONE

                // Set cursor from the product table where (watched == '1')
                cursor = productHelper.readDataWhereInt("product_watch", 1)
            }
            else->{
                cursor = productHelper.readAllData()
            }
        }

        // Convert cursor to list of products
        var productList: MutableList<Product> = mutableListOf()
        while (cursor.moveToNext()) {
            productList.add(Product(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                cursor.getString(3), cursor.getInt(4), cursor.getInt(5), cursor.getInt(6)))
        }

        // Set recycler list View
        val listView = view?.findViewById<RecyclerView>(R.id.recyclerView)
        val listeners = mutableListOf<(Product)->Unit>()
        listeners.add({showProductDetails(it)})
        listeners.add({deleteProduct(it)})
        listView?.adapter = ProductListAdapter (productList, listeners)
        listView?.layoutManager = LinearLayoutManager(context)
    }

    // Listener for product click
    fun showProductDetails(item: Product) {
        val intent = Intent(context, ProductActivity::class.java)
        intent.putExtra("activity", "viewProduct")
        intent.putExtra("productId", item.id)
        intent.putExtra("productName", item.name)
        ContextCompat.startActivity(requireContext(), intent, null)
    }

    // Listener for product delete button click
    fun deleteProduct(item: Product) {
        // Get product helper
        val productHelper = ProductHelper(context)

        // Delete product
        productHelper.deleteOneRow(item.id!!)

        // Reload list
        onResume()

        // Delete history
        val historyHelper = HistoryHelper(context)
        val productHistory = historyHelper.readDataWhereInt("product_id", item.id.toInt())

        historyHelper.deleteOneRow(item.id)
    }

}