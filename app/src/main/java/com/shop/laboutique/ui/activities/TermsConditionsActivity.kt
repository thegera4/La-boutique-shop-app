package com.shop.laboutique.ui.activities

import android.os.Bundle
import com.shop.laboutique.R
import com.shop.laboutique.databinding.ActivityTermsConditionsBinding

class TermsConditionsActivity : BaseActivity() {

    private lateinit var binding: ActivityTermsConditionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTermsConditionsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        noActionBar()

        setupActionBar()

    }

    private fun setupActionBar(){
        setSupportActionBar(binding.toolbarTermsActivity)
        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24)
        }
        binding.toolbarTermsActivity.setNavigationOnClickListener { onBackPressed() }
    }
}