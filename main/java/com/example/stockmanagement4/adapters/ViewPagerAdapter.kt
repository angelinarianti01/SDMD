package com.example.stockmanagement4.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.stockmanagement4.fragments.HomeFragment
import com.example.stockmanagement4.fragments.ListFragment
import com.example.stockmanagement4.fragments.HistoryTableFragment

class ViewPagerAdapter(fragmentManager: FragmentManager,lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        when(position){
            0->{
                //maybe change this to refer to main functions?
                var args = Bundle()
                args.putString("text", "hello")
                val home = HomeFragment()
                home.arguments = args
                return home
            }
            1->{
                var args = Bundle()
                args.putString("table", "products")
                args.putString("filter", "")
                val products = ListFragment()
                products.arguments = args
                return products
            }
            2->{
                var args = Bundle()
                args.putString("table", "history")
                val history = HistoryTableFragment()
                history.arguments = args
                return history
            }
            3->{
                var args = Bundle()
                args.putString("table", "products")
                args.putString("filter", "watchlist")
                val watchlist = ListFragment()
                watchlist.arguments = args
                return watchlist
            }
            else->{
                return Fragment()
            }

        }
    }
}