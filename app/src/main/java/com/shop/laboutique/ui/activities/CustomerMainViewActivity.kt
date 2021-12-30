package com.shop.laboutique.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.shop.laboutique.R
import com.shop.laboutique.databinding.ActivityCustomerMainViewBinding
import com.shop.laboutique.ui.activities.adapters.DashboardItemsListAdapter
import com.shop.laboutique.ui.activities.firebase.FirebaseClass
import com.shop.laboutique.ui.activities.models.Product

class CustomerMainViewActivity : BaseActivity() {

    private lateinit var binding: ActivityCustomerMainViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerMainViewBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupActionBar()


    }

    override fun onResume() {
        super.onResume()
        getDashboardItemsList()
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarCustomerMainView)
        val actionBar = supportActionBar
        /*if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24)
        }*/
        binding.toolbarCustomerMainView.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.dashboard_customer_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                return true
            }
            R.id.action_cart -> {
                startActivity(Intent(this, CartListActivity::class.java))
                return true
            }
            R.id.action_orders -> {
                startActivity(Intent(this, OrdersActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun successDashboardItemsListCustomer(dashboardItemsList: ArrayList<Product>){
        hideProgressDialog()

        if (dashboardItemsList.size > 0){
            binding.rvDashboardItems.visibility = View.VISIBLE
            binding.tvDashItemsFound.visibility = View.GONE

            binding.rvDashboardItems.layoutManager = GridLayoutManager(this, 2)
            binding.rvDashboardItems.setHasFixedSize(true)

            val adapter = DashboardItemsListAdapter(this, dashboardItemsList)
            binding.rvDashboardItems.adapter = adapter


        } else {
            binding.rvDashboardItems.visibility = View.GONE
            binding.tvDashItemsFound.visibility = View.VISIBLE
        }

    }

    private fun getDashboardItemsList(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FirebaseClass().getDashboardItemsListCatalogCustomer(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        FirebaseClass().deleteCollection()
    }

    override fun onBackPressed() {
        doubleBackToExit()
    }

}