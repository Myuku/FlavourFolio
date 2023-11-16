package com.example.flavourfolio

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MainFragmentStateAdapter(activity: FragmentActivity, var fragList: ArrayList<Fragment>)
    : FragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment {
        return fragList[position]
    }

    override fun getItemCount(): Int {
        return fragList.size
    }
}
