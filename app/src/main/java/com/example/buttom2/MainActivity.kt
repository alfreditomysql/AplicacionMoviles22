package com.example.buttom2

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.buttom2.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var selectTab = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = ViewPagerAdapter(this)

        // Obtener referencia al ImageView de la lupa utilizando View Binding
        val lupaImageView: ImageView = binding.lupa


        // Agregar OnClickListener al ImageView de la lupa
        lupaImageView.setOnClickListener {
            // Abrir SearchActivity cuando se haga clic en la lupa
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        val tabIcons = listOf(
            R.drawable.baseline_home_s_24,
            R.drawable.baseline_favorite_s_24,
            R.drawable.baseline_notifications_s_24,
            R.drawable.baseline_profile_s_24
        )

        val layouts = listOf(
            binding.homeLayout,
            binding.likeLayout,
            binding.notificationLayout,
            binding.profileLayout,

        )

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                selectTab = position
                updateTabIcons(tabIcons[position], layouts[position])
            }
        })

        setupTab(binding.homeLayout, 0, viewPager)
        setupTab(binding.likeLayout, 1, viewPager)
        setupTab(binding.notificationLayout, 2, viewPager)
        setupTab(binding.profileLayout, 3, viewPager)

        // Inicializar con la pestaña home
        viewPager.setCurrentItem(0, false)
    }

    private fun setupTab(layout: LinearLayout, tabIndex: Int, viewPager: ViewPager2) {
        layout.setOnClickListener {
            if (selectTab != tabIndex) {
                viewPager.setCurrentItem(tabIndex, true)
            }
        }
    }

    private fun updateTabIcons(selectedIcon: Int, selectedLayout: LinearLayout) {
        val icons = mapOf(
            binding.homeIv to R.drawable.baseline_home_24,
            binding.likeIv to R.drawable.baseline_favorite_24,
            binding.notificationIv to R.drawable.baseline_notifications_24,
            binding.profileIv to R.drawable.baseline_profile_24
        )

        val selectedIcons = mapOf(
            R.drawable.baseline_home_s_24 to binding.homeIv,
            R.drawable.baseline_favorite_s_24 to binding.likeIv,
            R.drawable.baseline_notifications_s_24 to binding.notificationIv,
            R.drawable.baseline_profile_s_24 to binding.profileIv
        )

        icons.forEach { (imageView, icon) ->
            imageView.setImageResource(icon)
        }

        selectedIcons[selectedIcon]?.setImageResource(selectedIcon)

        listOf(binding.homeLayout, binding.likeLayout, binding.notificationLayout, binding.profileLayout).forEach {
            it.setBackgroundResource(android.R.color.transparent)
        }

        selectedLayout.setBackgroundResource(getBackgroundResource(selectedIcon))
        animateTab(selectedLayout)
    }

    private fun getBackgroundResource(selectedIcon: Int): Int {
        return when (selectedIcon) {
            R.drawable.baseline_home_s_24 -> R.drawable.round_back_home_100
            R.drawable.baseline_favorite_s_24 -> R.drawable.round_back_favorite_100
            R.drawable.baseline_notifications_s_24 -> R.drawable.round_back_notification_100
            R.drawable.baseline_profile_s_24 -> R.drawable.round_back_profile_100
            else -> android.R.color.transparent
        }
    }

    private fun animateTab(layout: LinearLayout) {
        val scaleAnimation = ScaleAnimation(
            0.8f, 1.0f, 1f, 1f,
            Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f
        )
        scaleAnimation.duration = 200
        scaleAnimation.fillAfter = true
        layout.startAnimation(scaleAnimation)
    }
}
