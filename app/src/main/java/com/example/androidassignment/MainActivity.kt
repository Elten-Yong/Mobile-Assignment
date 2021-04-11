package com.example.androidassignment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(){



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val infoFragment = admin_info_center()
        val profileFragment = profile()
        val communityFragment = CommunityActivity()

        makeCurrentFragment(infoFragment)

        bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId){
                R.id.infoCenter -> makeCurrentFragment(infoFragment)
                R.id.profile -> makeCurrentFragment(profileFragment)
                R.id.community -> makeCurrentFragment(communityFragment)
            }
            true
        }
    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }
}