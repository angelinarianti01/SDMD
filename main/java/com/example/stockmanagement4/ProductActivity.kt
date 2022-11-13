package com.example.stockmanagement4

import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.cardview.widget.CardView
import com.example.stockmanagement4.databasehelpers.ProductHelper
import com.example.stockmanagement4.fragments.PQTableFragment
import com.example.stockmanagement4.models.Product

class ProductActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
    }

    override fun onResume() {
        super.onResume()

        // Database
        val productHelper : ProductHelper = ProductHelper(this)

        // Passed data
        val activity = intent.getStringExtra("activity").toString()
        val productName = intent.getStringExtra("productName").toString()
        val productId = intent.getStringExtra("productId").toString()

        // If user wants to view product, then product quantity will be shown
        if (activity == "viewProduct") {
            // Get Product from DB
            val productCursor : Cursor = productHelper.readDataWhereString("_id", productId)
            lateinit var product : Product
            if (productCursor.moveToNext()) {
                product = Product(productCursor.getString(0), productCursor.getString(1), productCursor.getString(2),
                    productCursor.getString(3), productCursor.getInt(4), productCursor.getInt(5), productCursor.getInt(6))
            }

            // Set pre-loaded prdocut data
            findViewById<EditText>(R.id.nameInput).setText(productName, TextView.BufferType.EDITABLE)
            findViewById<EditText>(R.id.categoryInput).setText(product.category, TextView.BufferType.EDITABLE)
            findViewById<EditText>(R.id.brandInput).setText(product.brand, TextView.BufferType.EDITABLE)
            findViewById<EditText>(R.id.costInput).setText(product.cost.toString(), TextView.BufferType.EDITABLE)
            findViewById<SwitchCompat>(R.id.watchInput).isChecked = if (product.watch == 1)  true else false
            findViewById<EditText>(R.id.minstockInput).setText(product.minstock.toString(), TextView.BufferType.EDITABLE)

            // Pre-loaded data
            val productName = intent.getStringExtra("productName").toString()
            val productId = intent.getStringExtra("productId").toString()

            // Product Quantity table
            var args = Bundle()
            args.putString("table", "productQuantity")
            args.putString("product_id", productId)
            val productQuantity = PQTableFragment()
            productQuantity.arguments = args
            supportFragmentManager.beginTransaction().add(R.id.pqTableContainer, productQuantity).commit()

            // Stock In
            val stockIn = findViewById<Button>(R.id.stockInButton)
            stockIn.setOnClickListener{
                val intent = Intent(this, StockActivity::class.java)
                intent.putExtra("productId", productId)
                intent.putExtra("productName", productName)
                intent.putExtra("stock", "in")
                startActivity(intent)
            }

            // Stock Out
            val stockOut = findViewById<Button>(R.id.stockOutButton)
            stockOut.setOnClickListener{
                val intent = Intent(this, StockActivity::class.java)
                intent.putExtra("productId", productId)
                intent.putExtra("productName", productName)
                intent.putExtra("stock", "out")
                startActivity(intent)
            }
        }
        // If user wants to add product, then product has no quantity yet
        else if (activity == "addProduct") {
            // Set table views to invisible
            findViewById<TextView>(R.id.productTableTitle).visibility = View.GONE
            findViewById<CardView>(R.id.tableCard).visibility = View.GONE
        }

        // On Save
        val saveButton = findViewById<Button>(R.id.saveButton)
        saveButton?.setOnClickListener{
            val nameInput = findViewById<EditText>(R.id.nameInput).text.toString()
            val categoryInput = findViewById<EditText>(R.id.categoryInput)?.text.toString()
            val brandInput = findViewById<EditText>(R.id.brandInput).text.toString()
            //implement integer check
            val costInput = findViewById<EditText>(R.id.costInput).text.toString().trim().toInt()
            val watchInput = findViewById<SwitchCompat>(R.id.watchInput).isChecked
            var watchInputBit = if (watchInput == true) 1 else 0
            val minstockInput = findViewById<EditText>(R.id.minstockInput).text.toString().trim().toInt()

            // Check intended activity
            if (activity == "addProduct") {
                productHelper.addProduct(Product("", nameInput, categoryInput, brandInput, costInput, watchInputBit, minstockInput))
            } else if (activity == "viewProduct") {
                productHelper.updateData(Product(productId, nameInput, categoryInput, brandInput, costInput, watchInputBit, minstockInput))
            }

            // Go back to Main activity
            finish()
        }

        // On cancel
        val cancelButton = findViewById<Button>(R.id.cancelButton)
        cancelButton.setOnClickListener{
            // Go back to Main activity
            finish()
        }
    }
}