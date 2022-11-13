package com.example.stockmanagement4.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.stockmanagement4.R
import com.example.stockmanagement4.databasehelpers.HistoryHelper
import com.example.stockmanagement4.databasehelpers.PQHelper
import com.example.stockmanagement4.databasehelpers.ProductHelper


class HistoryTableFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history_table, container, false)
    }

    override fun onResume() {
        super.onResume()
        // Table layout
        val tableLayout = view?.findViewById<TableLayout>(R.id.historyTable)
        val trParams = TableLayout.LayoutParams(
            TableLayout.LayoutParams.MATCH_PARENT,
            TableLayout.LayoutParams.WRAP_CONTENT,
        )

        // Clear table
        tableLayout?.removeAllViews()

        // Table title
        val titleRow = TableRow(context)

        val tv_id = TextView(context)
        tv_id.gravity = Gravity.CENTER
        tv_id.text = "Product Id"
        tv_id.textSize = 18F
        tv_id.setPadding(0, 0, 0, 20)
        tv_id.setTextColor(Color.BLACK)
        titleRow.addView(tv_id)

        val tv_name = TextView(context)
        tv_name.gravity = Gravity.CENTER
        tv_name.text = "Name"
        tv_name.textSize = 18F
        tv_name.setPadding(0, 0, 0, 10)
        tv_name.setTextColor(Color.BLACK)
        titleRow.addView(tv_name)

        val tv_date = TextView(context)
        tv_date.gravity = Gravity.CENTER
        tv_date.text = "Operated On"
        tv_date.textSize = 18F
        tv_date.setPadding(0, 0, 0, 10)
        tv_date.setTextColor(Color.BLACK)
        titleRow.addView(tv_date)

        val tv_qty = TextView(context)
        tv_qty.gravity = Gravity.CENTER
        tv_qty.text = "Quantity"
        tv_qty.textSize = 18F
        tv_qty.setPadding(0, 0, 0, 10)
        tv_qty.setTextColor(Color.BLACK)
        titleRow.addView(tv_qty)

        tableLayout?.addView(titleRow, trParams);

        // Read history data from database
        val historyHelper : HistoryHelper = HistoryHelper(context)
        val productHelper : ProductHelper = ProductHelper(context)
        val pqHelper : PQHelper = PQHelper(context)
        var cursor = historyHelper.readAllData()

        while (cursor.moveToNext()) {

            // Retrieve product name - this could have been input as field but for performance is not done
            lateinit var productName: String
            val cursor2 = productHelper.readDataWhereInt("_id", cursor.getInt(0))
            if (cursor2.moveToNext()) {
                productName = cursor2.getString(1)
            }

            // Create row
            val tr = TableRow(context)

            // Id
            val tv_id = TextView(context)
            tv_id.gravity = Gravity.CENTER
            tv_id.text = cursor.getString(0)
            tv_id.textSize = 20F
            tr.addView(tv_id)

            // Name
            val tv_name = TextView(context)
            tv_name.gravity = Gravity.CENTER
            tv_name.text = productName
            tv_name.textSize = 20F
            tr.addView(tv_name)

            // Date
            val tv_date = TextView(context)
            tv_date.gravity = Gravity.CENTER
            tv_date.text = cursor.getString(1)
            tv_date.textSize = 20F
            tr.addView(tv_date)

            // Quantity
            val tv_qty = TextView(context)
            val qty = cursor.getString(2)
            tv_qty.gravity = Gravity.CENTER
            tv_qty.text = qty
            tv_qty.textSize = 20F
            // Set stock in colour to green and stock out to red
            if (qty.toInt() > 0) {
                tv_qty.setTextColor(Color.GREEN)
            }
            else {
                tv_qty.setTextColor(Color.RED)
            }
            tr.addView(tv_qty)

            // Add row to table layout
            tableLayout?.addView(tr, trParams);
        }
    }
}