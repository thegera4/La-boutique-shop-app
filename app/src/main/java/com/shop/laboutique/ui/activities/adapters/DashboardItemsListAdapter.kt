package com.shop.laboutique.ui.activities.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shop.laboutique.R
import com.shop.laboutique.ui.activities.ProductDetailsActivity
import com.shop.laboutique.ui.activities.models.Product
import com.shop.laboutique.ui.activities.utils.Constants
import com.shop.laboutique.ui.activities.utils.GlideLoader

open class DashboardItemsListAdapter(
    private val context: Context,
    private val list: ArrayList<Product>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: View.OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_dashboard_layout, parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder){
            GlideLoader(context).loadProductPicture(model.image!!, holder.ivDashboardItemImage)
            holder.tvDashboardItemTitle.text = model.title
            holder.tvDashboardItemPrice.text = "$${model.price}"
            holder.itemView.setOnClickListener {
                holder.itemView.setOnClickListener {
                    val intent = Intent(context, ProductDetailsActivity::class.java)
                    intent.putExtra(Constants.EXTRA_PRODUCT_ID, model.product_id)
                    intent.putExtra(Constants.EXTRA_PRODUCT_OWNER_ID, model.user_id)
                    context.startActivity(intent)
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var ivDashboardItemImage: ImageView= view.findViewById(R.id.ivDashboardItemImage)
        var tvDashboardItemTitle: TextView = view.findViewById(R.id.tvDashboardItemTitle)
        var tvDashboardItemPrice: TextView = view.findViewById(R.id.tvDashboardItemPrice)

    }

    fun setOnClickListener(onClickListener: View.OnClickListener){
        this.onClickListener = onClickListener
    }

}