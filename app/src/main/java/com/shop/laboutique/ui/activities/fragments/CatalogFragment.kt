package com.shop.laboutique.ui.activities.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.GridLayoutManager
import com.shop.laboutique.R
import com.shop.laboutique.databinding.FragmentCatalogBinding
import com.shop.laboutique.ui.activities.CartListActivity
import com.shop.laboutique.ui.activities.SettingsActivity
import com.shop.laboutique.ui.activities.adapters.DashboardItemsListAdapter
import com.shop.laboutique.ui.activities.firebase.FirebaseClass
import com.shop.laboutique.ui.activities.models.Product

class CatalogFragment : BaseFragment() {

private var _binding: FragmentCatalogBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    //if we want to use the option menu in fragment we need to add it
    setHasOptionsMenu(true)
  }

  override fun onResume() {
    super.onResume()
    getDashboardItemsList()
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {

    _binding = FragmentCatalogBinding.inflate(inflater, container, false)

    return binding.root
  }

override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.dashboard_menu, menu)
    super.onCreateOptionsMenu(menu, inflater)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when(item.itemId){
      R.id.action_settings -> {
        startActivity(Intent(activity, SettingsActivity::class.java))
        return true
      }
      R.id.action_cart -> {
        startActivity(Intent(activity, CartListActivity::class.java))
        return true
      }
    }
    return super.onOptionsItemSelected(item)
  }

  fun successDashboardItemsList(dashboardItemsList: ArrayList<Product>){
    hideProgressDialog()

    if (dashboardItemsList.size > 0){
      binding.rvDashboardItems.visibility = View.VISIBLE
      binding.tvDashItemsFound.visibility = View.GONE

      binding.rvDashboardItems.layoutManager = GridLayoutManager(activity, 2)
      binding.rvDashboardItems.setHasFixedSize(true)

      val adapter = DashboardItemsListAdapter(requireActivity(), dashboardItemsList)
      binding.rvDashboardItems.adapter = adapter


    } else {
      binding.rvDashboardItems.visibility = View.GONE
      binding.tvDashItemsFound.visibility = View.VISIBLE
    }

  }

  private fun getDashboardItemsList(){
    showProgressDialog(resources.getString(R.string.please_wait))
    FirebaseClass().getDashboardItemsListCatalog(this@CatalogFragment)
  }

}

