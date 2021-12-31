package com.shop.laboutique.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shop.laboutique.R
import com.shop.laboutique.databinding.ActivityLoginBinding
import com.shop.laboutique.ui.activities.firebase.FirebaseClass
import com.shop.laboutique.ui.activities.models.User
import com.shop.laboutique.ui.activities.utils.Constants

class LoginActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        noActionBar()

        binding.tvForgotPassword.setOnClickListener(this)
        binding.btnLogin.setOnClickListener(this)
        binding.tvRegister.setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        if (view != null){
            when (view.id){

                R.id.tvForgotPassword ->
                    startActivity(Intent(this, ForgotPasswordActivity::class.java))

                R.id.btnLogin -> loginRegisteredUser()

                R.id.tvRegister ->
                    startActivity(Intent(this, RegisterActivity::class.java))
            }
        }
    }

    private fun loginRegisteredUser(){

        if (validateLoginDetails()){

            showProgressDialog(resources.getString(R.string.please_wait))

            val email = binding.etMail.text.toString().trim { it <= ' ' }
            val password = binding.etPassword.text.toString().trim { it <= ' ' }

            //Login using FirebaseAuth
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        FirebaseClass().getUserDetails(this@LoginActivity)
                    } else {
                        hideProgressDialog()
                        showErrorSnackBar(resources.getString(R.string.err_pass), true)
                    }

                }
        }
    }

    private fun validateLoginDetails(): Boolean {
        return when {
            TextUtils.isEmpty(binding.etMail.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            TextUtils.isEmpty(binding.etPassword.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            else -> {
                true
            }
        }
    }

    fun userLoggedInSuccess(user: User){
        hideProgressDialog()

        if (user.profileCompleted == 0){
                val intent = Intent(this@LoginActivity, UserProfileActivity::class.java)
                intent.putExtra(Constants.EXTRA_USER_DETAILS, user)
                startActivity(intent)
        } else {
            val user = Firebase.auth.currentUser
            user?.let {
                // The user's ID, unique to the Firebase project. Do NOT use this value to
                // authenticate with your backend server, if you have one. Use
                // FirebaseUser.getToken() instead.
                val uid = user.uid
                if (uid == "w80Bk2YDL8T0TLskAyVmF16CsFG2"){
                    startActivity(Intent(this@LoginActivity,
                        DashboardActivity::class.java))

                } else {
                    startActivity(Intent(this@LoginActivity,
                        CustomerMainViewActivity::class.java))
                }
            }
        }
        finish()
    }

    //TODO AGREGAR SHARED PREFERENCES CON SWITCH BUTTON

}