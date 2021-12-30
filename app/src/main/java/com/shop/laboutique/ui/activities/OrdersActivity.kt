package com.shop.laboutique.ui.activities

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.shop.laboutique.R
import com.shop.laboutique.databinding.ActivityOrdersBinding
import com.shop.laboutique.ui.activities.adapters.MyOrdersListAdapter
import com.shop.laboutique.ui.activities.firebase.FirebaseClass
import com.shop.laboutique.ui.activities.models.Order

class OrdersActivity : BaseActivity() {

    private lateinit var binding: ActivityOrdersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrdersBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupActionBar()


    }

    override fun onResume() {
        super.onResume()
        getMyOrdersListCustomer()
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarCustomerOrdersView)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24)
        }
        binding.toolbarCustomerOrdersView.setNavigationOnClickListener { onBackPressed() }
    }

    private fun getMyOrdersListCustomer() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirebaseClass().getMyOrdersListCustomer(this)
    }

    fun populateOrdersListInUI(ordersList: ArrayList<Order>) {

        hideProgressDialog()

        if (ordersList.size > 0) {

            binding.rvMyOrderItems.visibility = View.VISIBLE
            binding.tvNoOrdersFound.visibility = View.GONE

            binding.rvMyOrderItems.layoutManager = LinearLayoutManager(this)
            binding.rvMyOrderItems.setHasFixedSize(true)

            val myOrdersAdapter = MyOrdersListAdapter(this, ordersList)
            binding.rvMyOrderItems.adapter = myOrdersAdapter
        } else {
            binding.rvMyOrderItems.visibility = View.GONE
            binding.tvNoOrdersFound.visibility = View.VISIBLE
        }

    }



}