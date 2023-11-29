package com.example.firebasechatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {
    private var bottomNavigation: BottomNavigationView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigation = findViewById(R.id.bgBottomNavigation)
        bottomNavigation?.selectedItemId = R.id.menu_chats
        bottomNavigation?.menu?.findItem(R.id.menu_chats)?.isChecked = true
        initView()
    }

    private fun initView() {
        val chatFragment = ChatFragment.newInstance("","")
        val profileFragment = ProfileFragment.newInstance("","")
        bottomNavigation?.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_chats -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout, chatFragment).commit()
                    true
                }

                R.id.menu_profile -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout, profileFragment).commit()
                    true
                }

                else -> false
            }
        }
        bottomNavigation?.selectedItemId = R.id.menu_chats
    }
}