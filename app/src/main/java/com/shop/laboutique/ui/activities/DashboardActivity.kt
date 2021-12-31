package com.shop.laboutique.ui.activities

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.shop.laboutique.R
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.shop.laboutique.databinding.ActivityDashboardBinding
import com.shop.laboutique.ui.activities.firebase.FirebaseClass

class DashboardActivity : BaseActivity() {

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*supportActionBar!!.setBackgroundDrawable(ContextCompat.getDrawable(
            this@DashboardActivity,R.drawable.app_gradient_color_background))*/

        binding = ActivityDashboardBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_dashboard,
                R.id.navigation_notifications
            )
        )
        //setupActionBarWithNavController(navController, appBarConfiguration)

        navView.setupWithNavController(navController)

    }

    override fun onDestroy() {
        super.onDestroy()
        FirebaseClass().deleteCollection()
    }

    override fun onBackPressed() {
        doubleBackToExit()
    }
}