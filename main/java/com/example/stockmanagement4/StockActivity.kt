package com.example.stockmanagement4

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.stockmanagement4.databasehelpers.HistoryHelper
import com.example.stockmanagement4.databasehelpers.PQHelper
import com.example.stockmanagement4.fragments.DatePickerFragment
import com.example.stockmanagement4.models.History
import com.example.stockmanagement4.models.ProductQuantity
import java.text.DateFormat
import java.util.*


class StockActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stock)

        // Intent args
        val intention = intent.getStringExtra("stock")
        Log.i("intention", intention.toString())
        val productId = intent.getStringExtra("productId")
        val productName = intent.getStringExtra("productName")
        val currentDate = "${Calendar.getInstance().get(Calendar.YEAR)}-${Calendar.getInstance().get(Calendar.MONTH)}-${Calendar.getInstance().get(Calendar.DATE)}"

        // Database
        val pqHelper : PQHelper = PQHelper(this)
        val historyHelper : HistoryHelper = HistoryHelper(this)

        // Set title
        if (intention == "in") {
            findViewById<TextView>(R.id.stockFormTitle).text = "Stock In"
        } else if (intention == "out") {
            // Change title
            findViewById<TextView>(R.id.stockFormTitle).text = "Stock Out"
        }

        // Display
        findViewById<EditText>(R.id.productNameDisplay).setText(productName, TextView.BufferType.NORMAL)
        findViewById<EditText>(R.id.deliveryDateInput).setText(currentDate, TextView.BufferType.EDITABLE)
        findViewById<EditText>(R.id.quantityInput).setText("0", TextView.BufferType.EDITABLE)

        // User Buttons
        val submitButton = findViewById<Button>(R.id.submitButton)
        val cancelButton = findViewById<Button>(R.id.cancelButton)

        // Date on click
        val calendarButton = findViewById<ImageButton>(R.id.calendarButton)
        calendarButton.setOnClickListener(View.OnClickListener {
            val datePicker = DatePickerFragment()
            datePicker.show(supportFragmentManager, "date picker")
        })

        // On Submit
        submitButton.setOnClickListener{
            // User Input
            val qtyInput = findViewById<EditText>(R.id.quantityInput).text
            val dateInput = findViewById<EditText>(R.id.deliveryDateInput).text

            if (intention != null) {
                Log.i("tag", intention)
            }
            if (intention.toString() == "in") {
                // Add product quantity
                pqHelper.addRecord(ProductQuantity(productId, dateInput.toString(), qtyInput.toString().toInt()))
                finish()

                // Add to history
                historyHelper.addRecord(History(productId, dateInput.toString(), +qtyInput.toString().toInt()))
            }
            else if (intention.toString() == "out") {
                // Find product quantity
                productId?.let{
                    val cursor = pqHelper.readDataWhereString("product_id", productId)

                    while (cursor.moveToNext()) {
                        if (cursor.getString(1) == dateInput.toString()) {
                            // Record found -> update quantity
                            val new_qty = cursor.getInt(2) - qtyInput.toString().toInt()

                            // Update record
                            if (new_qty >= 0) {
                                pqHelper.updateData(ProductQuantity(productId, cursor.getString(1), new_qty))
                                Toast.makeText(this, "Sucessfully stock out ${new_qty} ${productName}", Toast.LENGTH_LONG).show()
                            }
                            else {
                                Toast.makeText(this, "Not enough quantity", Toast.LENGTH_SHORT).show()
                            }

                            // Go back to previous activity
                            finish()
                        }
                        else {
                            // Record not found
                            Toast.makeText(this, "Record not found", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                // Add to history
                historyHelper.addRecord(History(productId, currentDate, qtyInput.toString().toInt()*-1))
            }
        }

        // On cancel
        cancelButton.setOnClickListener{
            finish()
        }
    }

    // On Date Listener
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {
        val c = Calendar.getInstance()
        c[Calendar.YEAR] = year
        c[Calendar.MONTH] = month
        c[Calendar.DAY_OF_MONTH] = day
        val currentDateString: String = "$year-${month+1}-$day"

        findViewById<EditText>(R.id.deliveryDateInput).setText(currentDateString, TextView.BufferType.EDITABLE)
    }
}