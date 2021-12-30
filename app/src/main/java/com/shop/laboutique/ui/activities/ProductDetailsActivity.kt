package com.shop.laboutique.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.shop.laboutique.R
import com.shop.laboutique.databinding.ActivityProductDetailsBinding
import com.shop.laboutique.ui.activities.firebase.FirebaseClass
import com.shop.laboutique.ui.activities.models.CartItem
import com.shop.laboutique.ui.activities.models.Product
import com.shop.laboutique.ui.activities.utils.Constants
import com.shop.laboutique.ui.activities.utils.GlideLoader

class ProductDetailsActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityProductDetailsBinding

    private var mProductId: String = ""

    private lateinit var mProductDetails: Product

    private var mProductOwnerId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupActionBar()

        if (intent.hasExtra(Constants.EXTRA_PRODUCT_ID)){
            mProductId = intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
        }

        if (intent.hasExtra(Constants.EXTRA_PRODUCT_OWNER_ID)){
            mProductOwnerId = intent.getStringExtra(Constants.EXTRA_PRODUCT_OWNER_ID)!!
        }

        if (FirebaseClass().getCurrentUserID() == mProductOwnerId){
            binding.btnAddToCart.visibility = View.GONE
            binding.btnGoToCart.visibility = View.GONE
        } else {
            binding.btnAddToCart.visibility = View.VISIBLE
        }

        getProductDetails()

        binding.btnAddToCart.setOnClickListener(this)
        binding.btnGoToCart.setOnClickListener(this)

    }


    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarProductDetailsActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24)
        }

        binding.toolbarProductDetailsActivity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun getProductDetails(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FirebaseClass().getProductDetails(this, mProductId)
    }

    @SuppressLint("SetTextI18n")
    fun productDetailsSuccess(product: Product){
        mProductDetails = product
        GlideLoader(this@ProductDetailsActivity)
            .loadUserPicture(product.image!!, binding.ivProductDetailImage)
        binding.tvProductDetailsTitle.text = product.title
        binding.tvProductDetailsPrice.text = "$${product.price}"
        binding.tvProductDetailsDescription.text = product.description
        binding.tvProductDetailsAvailableQuantity.text = product.stock_quantity

        if(product.stock_quantity!!.toInt() == 0){
            hideProgressDialog()

            binding.btnAddToCart.visibility = View.GONE
            binding.tvProductDetailsQuantity.text = resources.getString(R.string.lbl_out_of_stock)

            binding.tvProductDetailsQuantity.setTextColor(
                ContextCompat.getColor
                (this@ProductDetailsActivity, R.color.colorSnackBarError))

        }else{
            // There is no need to check the cart list if the product owner himself is seeing the product details.
            if (FirebaseClass().getCurrentUserID() == product.user_id) {
                hideProgressDialog()
            } else {
                FirebaseClass().checkIfItemExistInCart(this@ProductDetailsActivity, mProductId)
            }
        }

    }

    private fun addToCart(){
        val cartItem = CartItem(
            FirebaseClass().getCurrentUserID(),
            mProductOwnerId,
            mProductId,
            mProductDetails.title,
            mProductDetails.price,
            mProductDetails.image,
            Constants.DEFAULT_CART_QUANTITY
        )
        showProgressDialog(resources.getString(R.string.please_wait))
        FirebaseClass().addCartItems(this, cartItem)

    }

    fun addToCartSuccess(){
        hideProgressDialog()
        Toast.makeText(this@ProductDetailsActivity,
            resources.getString(R.string.success_msg_item_added_to_cart),
            Toast.LENGTH_SHORT).show()

        binding.btnAddToCart.visibility = View.GONE
        binding.btnGoToCart.visibility = View.VISIBLE
    }

    fun productExistsInCart(){
        hideProgressDialog()
        binding.btnAddToCart.visibility = View.GONE
        binding.btnGoToCart.visibility = View.VISIBLE
    }

    override fun onClick(view: View?) {
       if (view != null){
            when(view.id){
                R.id.btnAddToCart -> addToCart()
                R.id.btnGoToCart -> {
                    startActivity(Intent(this@ProductDetailsActivity, CartListActivity::class.java))
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK//
                    finish()
                }
            }
        }
    }


}