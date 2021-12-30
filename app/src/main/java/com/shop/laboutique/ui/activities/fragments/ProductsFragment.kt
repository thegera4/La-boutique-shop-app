package com.shop.laboutique.ui.activities.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shop.laboutique.R
import com.shop.laboutique.databinding.FragmentProductsBinding
import com.shop.laboutique.ui.activities.AddProductActivity
import com.shop.laboutique.ui.activities.adapters.MyProductsListAdapter
import com.shop.laboutique.ui.activities.firebase.FirebaseClass
import com.shop.laboutique.ui.activities.models.Product
import com.shop.laboutique.ui.activities.utils.SwipeToDeleteFragments

class ProductsFragment : BaseFragment() {

private var _binding: FragmentProductsBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  fun productDeleteSuccess(){
    hideProgressDialog()
    Toast.makeText(requireActivity(), resources.getString(R.string.product_delete_success_message),
      Toast.LENGTH_SHORT).show()
    getProductListFromFireStore()
  }

  fun successProductListFromFireStore(productList: ArrayList<Product>){
    hideProgressDialog()

    if (productList.size > 0){
      binding.rvProducts.visibility = View.VISIBLE
      binding.tvNoProducts.visibility = View.GONE

      binding.rvProducts.layoutManager = LinearLayoutManager(activity)
      binding.rvProducts.setHasFixedSize(true)

      val adapterProducts = MyProductsListAdapter(requireActivity(), productList)
      binding.rvProducts.adapter = adapterProducts

      //swipe para eliminar producto agregado
      val deleteSwipeHandler = object : SwipeToDeleteFragments() {
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
          showProgressDialog(resources.getString(R.string.please_wait))

          FirebaseClass().deleteProduct(
            this@ProductsFragment,productList[viewHolder.adapterPosition].product_id!!)

        }

      }
      val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
      deleteItemTouchHelper.attachToRecyclerView(binding.rvProducts)


    } else {
      binding.rvProducts.visibility = View.GONE
      binding.tvNoProducts.visibility = View.VISIBLE
    }

  }

  private fun getProductListFromFireStore(){
    showProgressDialog(resources.getString(R.string.please_wait))
    FirebaseClass().getProductsList(this)
  }

  override fun onResume() {
    super.onResume()
    getProductListFromFireStore()
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {

    _binding = FragmentProductsBinding.inflate(inflater, container, false)
    val root: View = binding.root

    binding.fabAddProduct.setOnClickListener {
      startActivity(Intent(activity, AddProductActivity::class.java))
    }

    return root
  }

override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}