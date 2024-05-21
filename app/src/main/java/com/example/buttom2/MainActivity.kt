package com.example.buttom2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.buttom2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private var selectTab = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val homeLayout: LinearLayout = binding.homeLayout
        val likeLayout: LinearLayout = binding.likeLayout
        val notificationLayout: LinearLayout = binding.notificationLayout
        val profileLayout: LinearLayout = binding.profileLayout

        val homeImage: ImageView = binding.homeIv
        val likeImage: ImageView = binding.likeIv
        val notificationImage: ImageView = binding.notificationIv
        val profileImage: ImageView = binding.profileIv

        val homeTxt: TextView = binding.homeTxt
        val likeTxt: TextView = binding.likeTxt
        val notificationTxt: TextView = binding.notificationTxt
        val profileTxt: TextView = binding.profileTxt

        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.fragmentContainer, HomeFragment::class.java, null)
            .commit()

        homeLayout.setOnClickListener {
            if (selectTab != 1) {

                supportFragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragmentContainer, HomeFragment::class.java, null)
                    .commit()

                likeTxt.visibility = View.GONE
                notificationTxt.visibility = View.GONE
                profileTxt.visibility = View.GONE

                likeImage.setImageResource(R.drawable.baseline_favorite_24)
                notificationImage.setImageResource(R.drawable.baseline_notifications_24)
                profileImage.setImageResource(R.drawable.baseline_profile_24)

                likeLayout.setBackgroundResource(android.R.color.transparent)
                notificationLayout.setBackgroundResource(android.R.color.transparent)
                profileLayout.setBackgroundResource(android.R.color.transparent)

                homeTxt.visibility = View.VISIBLE
                homeImage.setImageResource(R.drawable.baseline_home_s_24)
                homeLayout.setBackgroundResource(R.drawable.round_back_home_100)

                val scaleAnimation = ScaleAnimation(
                    0.8f, 1.0f, 1f, 1f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f
                )
                scaleAnimation.duration = 200
                scaleAnimation.fillAfter = true
                homeLayout.startAnimation(scaleAnimation)

                selectTab = 1
            }
        }

        likeLayout.setOnClickListener {
            if (selectTab != 2) {

                supportFragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragmentContainer, LikeFragment::class.java, null)
                    .commit()

                homeTxt.visibility = View.GONE
                notificationTxt.visibility = View.GONE
                profileTxt.visibility = View.GONE

                homeImage.setImageResource(R.drawable.baseline_home_24)
                notificationImage.setImageResource(R.drawable.baseline_notifications_24)
                profileImage.setImageResource(R.drawable.baseline_profile_24)

                homeLayout.setBackgroundResource(android.R.color.transparent)
                notificationLayout.setBackgroundResource(android.R.color.transparent)
                profileLayout.setBackgroundResource(android.R.color.transparent)

                likeTxt.visibility = View.VISIBLE
                likeImage.setImageResource(R.drawable.baseline_favorite_s_24)
                likeLayout.setBackgroundResource(R.drawable.round_back_favorite_100)

                val scaleAnimation = ScaleAnimation(
                    0.8f, 1.0f, 1f, 1f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f
                )
                scaleAnimation.duration = 200
                scaleAnimation.fillAfter = true
                likeLayout.startAnimation(scaleAnimation)

                selectTab = 2
            }
        }

        notificationLayout.setOnClickListener {
            if (selectTab != 3) {

                supportFragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragmentContainer, NotificationFragment::class.java, null)
                    .commit()

                homeTxt.visibility = View.GONE
                likeTxt.visibility = View.GONE
                profileTxt.visibility = View.GONE

                homeImage.setImageResource(R.drawable.baseline_home_24)
                likeImage.setImageResource(R.drawable.baseline_favorite_24)
                profileImage.setImageResource(R.drawable.baseline_profile_24)

                homeLayout.setBackgroundResource(android.R.color.transparent)
                likeLayout.setBackgroundResource(android.R.color.transparent)
                profileLayout.setBackgroundResource(android.R.color.transparent)

                notificationTxt.visibility = View.VISIBLE
                notificationImage.setImageResource(R.drawable.baseline_notifications_s_24)
                notificationLayout.setBackgroundResource(R.drawable.round_back_notification_100)

                val scaleAnimation = ScaleAnimation(
                    0.8f, 1.0f, 1f, 1f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f
                )
                scaleAnimation.duration = 200
                scaleAnimation.fillAfter = true
                notificationLayout.startAnimation(scaleAnimation)

                selectTab = 3
            }
        }

        profileLayout.setOnClickListener {
            if (selectTab != 4) {

                supportFragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragmentContainer, ProfileFragment::class.java, null)
                    .commit()

                homeTxt.visibility = View.GONE
                likeTxt.visibility = View.GONE
                notificationTxt.visibility = View.GONE

                homeImage.setImageResource(R.drawable.baseline_home_24)
                likeImage.setImageResource(R.drawable.baseline_favorite_24)
                notificationImage.setImageResource(R.drawable.baseline_notifications_24)

                homeLayout.setBackgroundResource(android.R.color.transparent)
                likeLayout.setBackgroundResource(android.R.color.transparent)
                notificationLayout.setBackgroundResource(android.R.color.transparent)

                profileTxt.visibility = View.VISIBLE
                profileImage.setImageResource(R.drawable.baseline_profile_s_24)
                profileLayout.setBackgroundResource(R.drawable.round_back_profile_100)

                val scaleAnimation = ScaleAnimation(
                    0.8f, 1.0f, 1f, 1f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f
                )
                scaleAnimation.duration = 200
                scaleAnimation.fillAfter = true
                profileLayout.startAnimation(scaleAnimation)

                selectTab = 4
            }
        }
    }
}