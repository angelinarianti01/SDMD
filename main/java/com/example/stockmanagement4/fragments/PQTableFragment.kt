package com.example.stockmanagement4.fragments

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import com.example.stockmanagement4.R
import com.example.stockmanagement4.databasehelpers.PQHelper

class PQTableFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pq_table, container, false)
    }

    override fun onResume() {
        super.onResume()
        onCreateView(layoutInflater, view as ViewGroup, Bundle())

        // Table layout
        val tableLayout = view?.findViewById<TableLayout>(R.id.productQuantityTable)
        val trParams = TableLayout.LayoutParams(
            TableLayout.LayoutParams.MATCH_PARENT,
            TableLayout.LayoutParams.WRAP_CONTENT,
        )

        // Clear table
        tableLayout?.removeAllViews()

        // Table title
        val tr = TableRow(context)

        val tv_date = TextView(context)
        tv_date.gravity = Gravity.CENTER
        tv_date.text = "Delivery Date"
        tv_date.textSize = 20F
        tr.addView(tv_date)

        val tv_qty = TextView(context)
        tv_qty.gravity = Gravity.CENTER
        tv_qty.text = "Quantity"
        tv_qty.textSize = 20F
        tr.addView(tv_qty)

        tableLayout?.addView(tr, trParams);

        // DB helper
        val pqHelper = PQHelper(context)

        // Read quantity of the selected product
        var cursor = pqHelper.readDataWhereString("product_id", arguments?.getString("product_id").toString())

        // Add to table
        while (cursor.moveToNext()) {

            val tr = TableRow(context)

            val tv_date = TextView(context)
            tv_date.gravity = Gravity.CENTER
            tv_date.text = cursor.getString(1)
            tv_date.textSize = 20F
            tr.addView(tv_date)

            val tv_qty = TextView(context)
            tv_qty.gravity = Gravity.CENTER
            tv_qty.text = cursor.getString(2)
            tv_qty.textSize = 20F
            tr.addView(tv_qty)


            tableLayout?.addView(tr, trParams);
        }
    }
}