package com.example.buttom2

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 4 // Número de fragmentos
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> LikeFragment()
            2 -> NotificationFragment()
            3 -> ProfileFragment()
            else -> throw IllegalStateException("Posición desconocida: $position")
        }
    }
}
