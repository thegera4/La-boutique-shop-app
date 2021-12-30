package com.shop.laboutique.ui.activities.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shop.laboutique.databinding.FragmentPurchasedBinding

class PurchasedFragment : BaseFragment() {

private var _binding: FragmentPurchasedBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPurchasedBinding.inflate(inflater, container, false)

        return binding.root
    }

override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}