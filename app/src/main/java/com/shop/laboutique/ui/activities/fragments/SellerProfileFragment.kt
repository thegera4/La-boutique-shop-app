package com.shop.laboutique.ui.activities.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.shop.laboutique.R
import com.shop.laboutique.databinding.FragmentSellerProfileBinding
import com.shop.laboutique.ui.activities.LoginActivity
import com.shop.laboutique.ui.activities.UserProfileActivity
import com.shop.laboutique.ui.activities.firebase.FirebaseClass
import com.shop.laboutique.ui.activities.models.User
import com.shop.laboutique.ui.activities.utils.Constants
import com.shop.laboutique.ui.activities.utils.GlideLoader


class SellerProfileFragment : BaseFragment() {

    private lateinit var mUserDetails: User

    private var _binding: FragmentSellerProfileBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSellerProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(activity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        binding.tvEdit.setOnClickListener {
            val intent = Intent(activity, UserProfileActivity::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS, mUserDetails)
            startActivity(intent)
        }

        return root
    }

    private fun getUserDetailsFragment(){
        showProgressDialog(resources.getString(R.string.please_wait))
        activity?.let { FirebaseClass().getUserDetailsFragment(this) }
    }

    @SuppressLint("SetTextI18n")
    fun userDetailsSuccessFragment(user: User){
        mUserDetails = user
        hideProgressDialog()
        activity?.let { GlideLoader(it).loadUserPicture(user.image!!, binding.ivUserPhoto) }
        binding.tvName.text = "${user.firstName} ${user.lastName}"
        binding.tvGender.text = user.gender
        binding.tvEmail.text = user.email
        binding.tvMobileNumber.text = "${user.mobile}"
    }

    override fun onResume() {
        super.onResume()
        getUserDetailsFragment()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}