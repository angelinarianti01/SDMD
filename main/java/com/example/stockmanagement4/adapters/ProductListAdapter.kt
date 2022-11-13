package com.example.stockmanagement4.adapters

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.stockmanagement4.MainActivity
import com.example.stockmanagement4.ProductActivity
import com.example.stockmanagement4.R
import com.example.stockmanagement4.StockActivity
import com.example.stockmanagement4.databasehelpers.PQHelper
import com.example.stockmanagement4.databasehelpers.ProductHelper
import com.example.stockmanagement4.fragments.ListFragment
import com.example.stockmanagement4.models.Product
import com.example.stockmanagement4.models.ProductQuantity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ProductListAdapter(private val list: List<Product>,
                         private val listeners: List<(Product)->Unit>) : RecyclerView.Adapter<ProductListAdapter.ViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductListAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.product_row, parent, false) as View
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductListAdapter.ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    inner class ViewHolder(val v: View): RecyclerView.ViewHolder(v) {
        val productId = v.findViewById<TextView>(R.id.productId)
        val productName = v.findViewById<TextView>(R.id.productName)
        val productCategory = v.findViewById<TextView>(R.id.productCategory)
        val productQuantity = v.findViewById<TextView>(R.id.productQuantity)

        fun bind(item: Product) {
            // Get quantity
            var qty = 0
            val pqHelper = PQHelper(v.context)
            var cursor = item.id?.let { pqHelper.readDataWhereString("product_id", it) }
            cursor?.let{ while (cursor.moveToNext()) {
                // Sum quantity
                qty += cursor.getInt(2)
            }}

            // Set data
            productId.text = item.id
            productName.text = item.name
            productCategory.text = item.category
            productQuantity.text = qty.toString()

            // Quantity is colored based on its stock level quantity
            // If it is less than the minimum stock, it's marked red
            if (qty < item.minstock!!) {
                productQuantity.setTextColor(Color.RED)
            }

            // Product details on click listener
            v.findViewById<LinearLayout>(R.id.productRow).setOnClickListener() {
                listeners[0](item)
            }

            v.findViewById<ImageButton>(R.id.deleteButton).setOnClickListener() {
                listeners[1](item)
            }
        }
    }
}