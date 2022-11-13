package com.example.stockmanagement4.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.stockmanagement4.MainActivity
import com.example.stockmanagement4.R
import com.example.stockmanagement4.StockActivity


class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Buttons clicks
        view.findViewById<CardView>(R.id.productsButton).setOnClickListener {
            var args = Bundle()
            args.putString("filter", "")
            val products = ListFragment()
            products.arguments = args
            (activity as MainActivity).navigateFragment(1)
        }
        view.findViewById<CardView>(R.id.watchlistButton).setOnClickListener {
            (activity as MainActivity).navigateFragment(3)
        }
        view.findViewById<CardView>(R.id.stockInButton).setOnClickListener {
            (activity as MainActivity).navigateFragment(1)
            val intent = Intent(context, StockActivity::class.java)
            intent.putExtra("Stock", "In")
            startActivity(intent)
        }
        view.findViewById<CardView>(R.id.stockOutButton).setOnClickListener {
            (activity as MainActivity).navigateFragment(1)
        }
    }

}