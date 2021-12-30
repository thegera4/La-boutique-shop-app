package com.shop.laboutique.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.shop.laboutique.R
import com.shop.laboutique.databinding.ActivityAddProductBinding
import com.shop.laboutique.ui.activities.firebase.FirebaseClass
import com.shop.laboutique.ui.activities.models.Product
import com.shop.laboutique.ui.activities.utils.Constants
import com.shop.laboutique.ui.activities.utils.GlideLoader
import java.io.IOException

class AddProductActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAddProductBinding

    private var mProductImageFileUri: Uri? = null
    private var mProductImageURL: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupActionBar()

        binding.ivAddUpdateImage.setOnClickListener(this)
        binding.btnSubmit.setOnClickListener(this)
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarAddProductActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24)
        }

        binding.toolbarAddProductActivity.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {

                R.id.iv_add_update_image -> {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE ) ==
                        PackageManager.PERMISSION_GRANTED) {

                        Constants.showImageChooser(this@AddProductActivity)

                    } else {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_PERMISSION_CODE
                        )

                    }

                }

                R.id.btn_submit -> {
                    if (validateProductDetails()){
                        uploadProductImage()
                    }
                }
            }
        }
    }

    private fun uploadProductImage(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FirebaseClass().uploadImageToCloudStorage(this, mProductImageFileUri, Constants.PRODUCT_IMAGE)
    }

    fun productUploadSuccess(){
        hideProgressDialog()
        Toast.makeText(this@AddProductActivity,
            resources.getString(R.string.product_uploaded_success_message),
            Toast.LENGTH_SHORT).show()
        finish()
    }

    fun imageUploadSuccess(imageURL: String){
        mProductImageURL = imageURL
        uploadProductDetails()
    }

    private fun uploadProductDetails(){
        val username = this.getSharedPreferences(Constants.USER_PREFERENCES, Context.MODE_PRIVATE)
            .getString(Constants.LOGGED_IN_USERNAME, "")!!

        val product = Product(FirebaseClass().getCurrentUserID(),
            username,
            binding.etProductTitle.text.toString().trim{ it <= ' '},
            binding.etProductPrice.text.toString().trim{ it <= ' '},
            binding.etProductDescription.text.toString().trim{ it <= ' '},
            binding.etProductQuantity.text.toString().trim{ it <= ' '},
            mProductImageURL)

        FirebaseClass().uploadProductDetails(this, product)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_PERMISSION_CODE){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Constants.showImageChooser(this)
            } else {
                Toast.makeText(this,
                    resources.getString(R.string.read_storage_permission_denied),
                    Toast.LENGTH_LONG).show()
            }
        }
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == Constants.PICK_IMAGE_PERMISSION_CODE){
                if (data != null) {
                    binding.ivAddUpdateImage.setImageDrawable(
                        ContextCompat.getDrawable(this,
                        R.drawable.ic_vector_edit))
                    mProductImageFileUri = data.data!!
                    try {
                        GlideLoader(this).loadUserPicture(mProductImageFileUri!!,
                            binding.ivProductImage)
                    } catch (e: IOException){
                        e.printStackTrace()
                    }
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED){
            Log.e("Request Cancelled", "Image selection cancelled")
        }
    }

    private fun validateProductDetails(): Boolean{
        return when{

            mProductImageFileUri == null -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_product_image), true)
                false
            }

            TextUtils.isEmpty(binding.etProductTitle.text.toString().trim{ it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_product_title), true)
                false
            }

            TextUtils.isEmpty(binding.etProductPrice.text.toString().trim{ it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_product_price), true)
                false
            }

            TextUtils.isEmpty(binding.etProductDescription.text.toString().trim{ it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_product_description), true)
                false
            }

            TextUtils.isEmpty(binding.etProductQuantity.text.toString().trim{ it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_product_quantity), true)
                false
            }

            else -> {
                true
            }
        }
    }
}