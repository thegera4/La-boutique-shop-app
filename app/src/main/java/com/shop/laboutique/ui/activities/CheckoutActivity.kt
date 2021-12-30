package com.shop.laboutique.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shop.laboutique.R
import com.shop.laboutique.databinding.ActivityCheckoutBinding
import com.shop.laboutique.ui.activities.adapters.CartItemsListAdapter
import com.shop.laboutique.ui.activities.firebase.FirebaseClass
import com.shop.laboutique.ui.activities.models.Address
import com.shop.laboutique.ui.activities.models.CartItem
import com.shop.laboutique.ui.activities.models.Order
import com.shop.laboutique.ui.activities.models.Product
import com.shop.laboutique.ui.activities.utils.Constants

class CheckoutActivity : BaseActivity() {

    private lateinit var binding: ActivityCheckoutBinding

    private var mAddressDetails: Address? = null

    private lateinit var mProductsList: ArrayList<Product>

    private lateinit var mCartItemsList: ArrayList<CartItem>

    private var mSubTotal: Double = 0.0

    private var mTotalAmount: Double = 0.0

    private lateinit var mOrderDetails: Order

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupActionBar()

        if (intent.hasExtra(Constants.EXTRA_SELECTED_ADDRESS)) {
            mAddressDetails =
                intent.getParcelableExtra(Constants.EXTRA_SELECTED_ADDRESS)!!
        }

        if (mAddressDetails != null) {
            binding.tvCheckoutAddressType.text = mAddressDetails?.type
            binding.tvCheckoutFullName.text = mAddressDetails?.name
            binding.tvCheckoutAddress.text = "${mAddressDetails!!.address}, ${mAddressDetails!!.zipCode}"
            binding.tvCheckoutAdditionalNote.text = mAddressDetails?.additionalNote
            if (mAddressDetails?.otherDetails!!.isNotEmpty()) {
                binding.tvCheckoutOtherDetails.text = mAddressDetails?.otherDetails
            }

            binding.tvCheckoutMobileNumber.text = mAddressDetails?.mobileNumber

        }

        binding.btnPlaceOrder.setOnClickListener { placeAnOrder() }

        getProductList()

    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarCheckoutActivity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24)
        }
        binding.toolbarCheckoutActivity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun getProductList() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirebaseClass().getAllProductsList(this@CheckoutActivity)
    }

    private fun getCartItemsList() {
        FirebaseClass().getCartList(this@CheckoutActivity)
    }

    fun successProductsListFromFireStore(productsList: ArrayList<Product>) {
        mProductsList = productsList
        getCartItemsList()
    }

    @SuppressLint("SetTextI18n")
    fun successCartItemsList(cartList: ArrayList<CartItem>) {
        hideProgressDialog()

        for (product in mProductsList) {
            for (cart in cartList) {
                if (product.product_id == cart.product_id) {
                    cart.stock_quantity = product.stock_quantity
                }
            }
        }

        mCartItemsList = cartList

        binding.rvCartListItems.layoutManager = LinearLayoutManager(this@CheckoutActivity)
        binding.rvCartListItems.setHasFixedSize(true)

        val cartListAdapter = CartItemsListAdapter(this@CheckoutActivity, mCartItemsList, false)
        binding.rvCartListItems.adapter = cartListAdapter

        for (item in mCartItemsList) {
            val availableQuantity = item.stock_quantity!!.toInt()
            if (availableQuantity > 0) {
                val price = item.price!!.toDouble()
                val quantity = item.cart_quantity!!.toInt()
                mSubTotal += (price * quantity)
            }
        }

        binding.tvCheckoutSubTotal.text = "$$mSubTotal"
        // Here we have kept Shipping Charge is fixed as $50 but in your case it may cary.
        binding.tvCheckoutShippingCharge.text = "$50.0"

        if (mSubTotal > 0) {
            binding.llCheckoutPlaceOrder.visibility = View.VISIBLE
            mTotalAmount = mSubTotal + 50
            binding.tvCheckoutTotalAmount.text = "$$mTotalAmount"
        } else {
            binding.llCheckoutPlaceOrder.visibility = View.GONE
        }


    }

    private fun placeAnOrder() {

        showProgressDialog(resources.getString(R.string.please_wait))

        mOrderDetails = Order(
            FirebaseClass().getCurrentUserID(),
            mCartItemsList,
            mAddressDetails!!,
            "Mi orden ${System.currentTimeMillis()}",
            mCartItemsList[0].image,
            mSubTotal.toString(),
            "50.0", // The Shipping Charge is fixed
            mTotalAmount.toString(),
            System.currentTimeMillis()
        )

        FirebaseClass().placeOrder(this@CheckoutActivity, mOrderDetails)

    }

    fun orderPlacedSuccess() {
        FirebaseClass().updateAllDetails(this@CheckoutActivity, mCartItemsList, mOrderDetails)
    }

    fun allDetailsUpdatedSuccessfully() {
        hideProgressDialog()
        Toast.makeText(
            this@CheckoutActivity, "Tu orden se ha creado correctamente.",
            Toast.LENGTH_SHORT
        ).show()

        finish()

        //se agrego para al momento de editar datos del perfil, te mande al dashboard de cliente
        val user = Firebase.auth.currentUser
        user?.let {
            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            val uid = user.uid
            if (uid == "w80Bk2YDL8T0TLskAyVmF16CsFG2") {
                startActivity(Intent(this, DashboardActivity::class.java))
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            } else {
                startActivity(Intent(this, CustomerMainViewActivity::class.java))
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

        }

    }

}