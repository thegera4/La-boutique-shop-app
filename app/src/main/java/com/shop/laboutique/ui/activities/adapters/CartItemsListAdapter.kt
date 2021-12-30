package com.shop.laboutique.ui.activities.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.shop.laboutique.R
import com.shop.laboutique.ui.activities.CartListActivity
import com.shop.laboutique.ui.activities.firebase.FirebaseClass
import com.shop.laboutique.ui.activities.models.CartItem
import com.shop.laboutique.ui.activities.utils.Constants
import com.shop.laboutique.ui.activities.utils.GlideLoader

open class CartItemsListAdapter (
    private val context: Context,
    private var list: ArrayList<CartItem>,
    private val updateCartItems: Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    /**
     * Inflates the item views which is designed in xml layout file
     *
     * create a new
     * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_cart_layout,
                parent,
                false
            )
        )
    }

    /**
     * Binds each item in the ArrayList to a view
     *
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     */
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {

            GlideLoader(context).loadProductPicture(model.image!!, holder.ivCartItemImage)

            holder.tvCartItemTitle.text = model.title
            holder.tvCartItemPrice.text = "$${model.price}"
            holder.tvCartQuantity.text = model.cart_quantity

            //Show the text out of stock when cart quantity is zero
            if (model.cart_quantity == "0") {
                holder.ibRemoveCartItem.visibility = View.GONE
                holder.ibAddCartItem.visibility = View.GONE

                holder.tvCartQuantity.text =
                    context.resources.getString(R.string.lbl_out_of_stock)
                holder.tvCartQuantity.setTextColor(ContextCompat.getColor
                    (context, R.color.colorSnackBarError)
                )
            } else {

                if (updateCartItems){
                    holder.ibRemoveCartItem.visibility = View.VISIBLE
                    holder.ibAddCartItem.visibility = View.VISIBLE

                } else {
                    holder.ibRemoveCartItem.visibility = View.GONE
                    holder.ibAddCartItem.visibility = View.GONE
                }
                holder.tvCartQuantity.setTextColor(ContextCompat.getColor
                    (context, R.color.colorSecondaryText))
            }

            holder.ibRemoveCartItem.setOnClickListener {

                //Call the update or remove function of firestore based on cart quantity
                if (model.cart_quantity == "1") {
                    FirebaseClass().removeItemFromCart(context, model.id!!)
                } else {
                    val cartQuantity: Int = model.cart_quantity!!.toInt()
                    val itemHashMap = HashMap<String, Any>()
                    itemHashMap[Constants.CART_QUANTITY] = (cartQuantity - 1).toString()

                    if (context is CartListActivity) {
                        context.showProgressDialog(context.resources.getString(R.string.please_wait))
                    }
                    FirebaseClass().updateMyCart(context, model.id!!, itemHashMap)

                }
            }

            holder.ibAddCartItem.setOnClickListener {
                // Call the update function of firestore class based on the cart quantity.
                val cartQuantity: Int = model.cart_quantity!!.toInt()
                if (cartQuantity < model.stock_quantity!!.toInt()) {
                    val itemHashMap = HashMap<String, Any>()
                    itemHashMap[Constants.CART_QUANTITY] = (cartQuantity + 1).toString()

                    if (context is CartListActivity) {
                        context.showProgressDialog(context.resources.getString(R.string.please_wait))
                    }
                    FirebaseClass().updateMyCart(context, model.id!!, itemHashMap)
                } else {
                    if (context is CartListActivity) {
                        context.showErrorSnackBar(
                            context.resources.getString(
                                R.string.msg_for_available_stock,
                                model.stock_quantity
                            ),
                            true
                        )
                    }
                }

            }


        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var ivCartItemImage: ImageView = view.findViewById(R.id.iv_cart_item_image)
        var tvCartItemTitle: TextView = view.findViewById(R.id.tv_cart_item_title)
        var tvCartItemPrice: TextView = view.findViewById(R.id.tv_cart_item_price)
        var tvCartQuantity: TextView = view.findViewById(R.id.tv_cart_quantity)
        var ibRemoveCartItem: ImageButton = view.findViewById(R.id.ib_remove_cart_item)
        var ibAddCartItem: ImageButton = view.findViewById(R.id.ib_add_cart_item)
    }
}

